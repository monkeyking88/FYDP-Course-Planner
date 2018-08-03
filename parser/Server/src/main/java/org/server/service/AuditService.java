package org.server.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.auditor.main.Auditor;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.common.audit.AuditResult;
import org.common.audit.AuditResult.AuditOutcome;
import org.common.audit.AuditResult.AuditSingleResult;
import org.server.persistence.BaseDao;
import org.server.response.DeferedMessage;
import org.server.response.DeferedMessage.AuditStatus;

import redis.clients.jedis.Jedis;

public class AuditService {
	
	public static final String REDIS_RESULT_PREFIX = "AUDIT_RESULT_";
	public static final String REDIS_AUDIT_ID_KEY = "AUDIT_ID";
	
	private static final Log log = LogFactory.getLog(AuditService.class);
	private static final ObjectMapper mapper = new ObjectMapper();
	
	
	public static DeferedMessage audit(final String transcript) throws JsonGenerationException, JsonMappingException, IOException{
		Jedis jedis = null;
		DeferedMessage result = new DeferedMessage();
		
		try {
			jedis = BaseDao.getJedis();
			long id = jedis.incr(REDIS_AUDIT_ID_KEY);
			final int newId = (int)id;
			
			//bootstrap data for Redis to setup for "In Progress" message
			AuditResult auditResult = new AuditResult();
			auditResult.addResult("CS", AuditOutcome.INPROGRESS, "Audit In Progress");
			String jsonStr = mapper.writeValueAsString(auditResult);
			jedis.set(REDIS_RESULT_PREFIX + newId, jsonStr);
			
			//do audit in a different thread, because it is computationally intensive and takes long time
			Thread auditThread = new Thread() {
			    public void run() {
			    	Jedis jedis = null;
			        try {
			        	jedis = BaseDao.getJedis();
			            
			            List<String> degree = new ArrayList<String>();
						degree.add("CS");
						
						Auditor auditor = new Auditor();
						AuditResult auditResult = auditor.handle(transcript);
						
						String jsonStr = mapper.writeValueAsString(auditResult);
						jedis.set(REDIS_RESULT_PREFIX + newId, jsonStr);

			        } catch(Exception e) {
			        	log.error("Audit Thread Error", e);
			        } finally {
			        	BaseDao.returnJedis(jedis);
			        }
			    }  
			};
			auditThread.start();
			
			result.setStatus(AuditStatus.CREATED).setId(newId);
		} finally {
			BaseDao.returnJedis(jedis);
		}
		
		return result;
	}
	
	public static DeferedMessage getResult(int id) throws JsonParseException, JsonMappingException, IOException{
		Jedis jedis = null;
		DeferedMessage result = new DeferedMessage();
		
		try {
			jedis = BaseDao.getJedis();
			String auditResultJSONStr = jedis.get(REDIS_RESULT_PREFIX + id);
			
			if (auditResultJSONStr == null){
				return result.setStatus(AuditStatus.NOTFOUND).setId(id);
			}
			
			AuditResult auditResult = mapper.readValue(auditResultJSONStr, AuditResult.class);
			AuditSingleResult singleResult = auditResult.getResults().get("CS");
			
			switch(singleResult.getOutcome()){
			case INPROGRESS:
				result.setStatus(AuditStatus.INPROGRESS).setId(id);
				break;
				
			default:
				result.setStatus(AuditStatus.FINISHED).setId(id).setResult(auditResult);
				break;
			}
		} finally {
			BaseDao.returnJedis(jedis);
		}
		return result;
	}

}