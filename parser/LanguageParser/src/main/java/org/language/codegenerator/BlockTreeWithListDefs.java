package org.language.codegenerator;

import java.util.Map;

import org.language.blocktree.BlockTree;

@Deprecated
public class BlockTreeWithListDefs {
	private BlockTree blockTree;
	private Map<String, CourseIdArray> listDefs;
	
	public BlockTreeWithListDefs(BlockTree blockTree,
			Map<String, CourseIdArray> listDefs) {
		super();
		this.blockTree = blockTree;
		this.listDefs = listDefs;
	}

	public BlockTree getBlockTree(){
		return blockTree;
	}

	public Map<String, CourseIdArray> getListDefs() {
		return listDefs;
	}
	
}
