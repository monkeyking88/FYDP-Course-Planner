-- MySQL dump 10.13  Distrib 5.5.31, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: advisor
-- ------------------------------------------------------
-- Server version	5.5.37-1-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `approval`
--

DROP TABLE IF EXISTS `approval`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `approval` (
  `uw_id` int(8) unsigned NOT NULL,
  `subject` varchar(8) NOT NULL,
  `catalog_nbr` varchar(20) NOT NULL,
  `sbj_list` varchar(8) NOT NULL,
  `cnbr_name` varchar(20) NOT NULL,
  `given_on` date NOT NULL,
  `given_by` varchar(8) NOT NULL,
  PRIMARY KEY (`uw_id`,`subject`,`catalog_nbr`,`sbj_list`,`cnbr_name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `audit`
--

DROP TABLE IF EXISTS `audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit` (
  `uw_id` int(8) unsigned NOT NULL,
  `plan` varchar(8) NOT NULL,
  `status` varchar(22) NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`uw_id`,`plan`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `checks`
--

DROP TABLE IF EXISTS `checks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `checks` (
  `uw_id` int(8) unsigned NOT NULL,
  `term_code` smallint(4) unsigned NOT NULL,
  `target` varchar(20) NOT NULL DEFAULT '',
  `change_text` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`uw_id`,`term_code`,`target`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `class`
--

DROP TABLE IF EXISTS `class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `class` (
  `term_code` smallint(4) unsigned NOT NULL,
  `class_nbr` smallint(4) unsigned NOT NULL,
  `subject` varchar(8) NOT NULL,
  `catalog_nbr` char(5) NOT NULL,
  `section` char(3) NOT NULL DEFAULT '',
  `section_type` char(3) NOT NULL DEFAULT 'LEC',
  `associated` smallint(2) unsigned NOT NULL DEFAULT '1',
  `title` varchar(127) NOT NULL DEFAULT '',
  `campus` varchar(8) DEFAULT NULL,
  `instructor` varchar(63) NOT NULL DEFAULT '',
  `units` float DEFAULT NULL,
  PRIMARY KEY (`term_code`,`class_nbr`),
  UNIQUE KEY `term_code` (`term_code`,`subject`,`catalog_nbr`,`section`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `course_au`
--

DROP TABLE IF EXISTS `course_au`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `course_au` (
  `subject` varchar(8) NOT NULL,
  `catalog_nbr` char(5) NOT NULL,
  `hours` float NOT NULL DEFAULT '0',
  `math` float NOT NULL DEFAULT '0',
  `sci` float NOT NULL DEFAULT '0',
  `cse` float NOT NULL DEFAULT '0',
  `engsci` float NOT NULL DEFAULT '0',
  `engdes` float NOT NULL DEFAULT '0',
  `other` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`subject`,`catalog_nbr`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `grade`
--

DROP TABLE IF EXISTS `grade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `grade` (
  `uw_id` int(8) unsigned NOT NULL,
  `term_code` smallint(4) unsigned NOT NULL,
  `class_nbr` smallint(4) unsigned NOT NULL,
  `grading_basis` char(3) DEFAULT NULL,
  `grade` char(3) DEFAULT NULL,
  `req_desig` char(4) DEFAULT NULL,
  `req_desig_grade` char(1) DEFAULT NULL,
  `rqmt_nbr_p10` int(10) unsigned DEFAULT NULL,
  `rqmt_nbr_p20` int(10) unsigned DEFAULT NULL,
  `rqmt_nbr_p30` int(10) unsigned DEFAULT NULL,
  `ticket` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`uw_id`,`term_code`,`class_nbr`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `grade_rqmt`
--

DROP TABLE IF EXISTS `grade_rqmt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `grade_rqmt` (
  `uw_id` int(8) unsigned NOT NULL,
  `term_code` smallint(4) unsigned NOT NULL,
  `class_nbr` smallint(4) unsigned NOT NULL,
  `rqmt_nbr` int(10) unsigned NOT NULL,
  UNIQUE KEY `uw_id` (`uw_id`,`term_code`,`class_nbr`,`rqmt_nbr`),
  KEY `uw_id_2` (`uw_id`,`term_code`,`class_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `instructor`
--

DROP TABLE IF EXISTS `instructor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instructor` (
  `last_name` varchar(50) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `department` varchar(100) NOT NULL,
  `faculty` varchar(50) NOT NULL,
  `peng_license` varchar(50) NOT NULL,
  `peng_jurisdiction` varchar(50) NOT NULL,
  `peng_status` varchar(50) NOT NULL,
  `peng_date` date DEFAULT NULL,
  PRIMARY KEY (`last_name`,`first_name`,`department`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `list`
--

DROP TABLE IF EXISTS `list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `list` (
  `calendar` smallint(4) unsigned zerofill NOT NULL DEFAULT '2005',
  `name` varchar(20) NOT NULL,
  `sbj_list` varchar(8) NOT NULL,
  `cnbr_name` varchar(20) NOT NULL,
  PRIMARY KEY (`calendar`,`name`,`sbj_list`,`cnbr_name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log` (
  `uwuserid` varchar(8) NOT NULL,
  `access_level` varchar(16) NOT NULL,
  `session_start` date NOT NULL,
  `ip_addr` varchar(39) NOT NULL,
  KEY `session_start` (`session_start`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `program`
--

DROP TABLE IF EXISTS `program`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `program` (
  `plan` varchar(8) NOT NULL,
  `calendar` smallint(4) unsigned zerofill NOT NULL,
  `category` varchar(8) NOT NULL,
  `sbj_list` varchar(8) NOT NULL,
  `cnbr_name` varchar(20) NOT NULL,
  `rqmt_nbr` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`rqmt_nbr`)
) ENGINE=MyISAM AUTO_INCREMENT=889 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `student` (
  `uw_id` int(8) unsigned NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `middle_name` varchar(50) NOT NULL DEFAULT '',
  `gender` char(1) DEFAULT NULL,
  `email` varchar(50) NOT NULL,
  `calendar` smallint(4) unsigned zerofill DEFAULT NULL,
  `audit_10` varchar(10) NOT NULL DEFAULT '',
  `audit_20` varchar(10) NOT NULL DEFAULT '',
  `audit_30` varchar(10) NOT NULL DEFAULT '',
  PRIMARY KEY (`uw_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `student_au`
--

DROP TABLE IF EXISTS `student_au`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `student_au` (
  `uw_id` int(8) unsigned NOT NULL,
  `math` float NOT NULL DEFAULT '0',
  `sci` float NOT NULL DEFAULT '0',
  `cse` float NOT NULL DEFAULT '0',
  `engsci` float NOT NULL DEFAULT '0',
  `engsci_peng` float NOT NULL DEFAULT '0',
  `engdes` float NOT NULL DEFAULT '0',
  `engdes_peng` float NOT NULL DEFAULT '0',
  `other` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`uw_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `term`
--

DROP TABLE IF EXISTS `term`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `term` (
  `uw_id` int(8) unsigned NOT NULL,
  `term_code` smallint(4) unsigned NOT NULL,
  `plan_10` varchar(10) NOT NULL,
  `plan_20` varchar(10) NOT NULL,
  `plan_30` varchar(10) NOT NULL,
  `term_level` char(2) NOT NULL,
  `standing` varchar(4) NOT NULL DEFAULT '',
  `awards` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`uw_id`,`term_code`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transfer`
--

DROP TABLE IF EXISTS `transfer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transfer` (
  `uw_id` int(8) unsigned NOT NULL,
  `institution` varchar(64) NOT NULL,
  `term_code` smallint(4) unsigned NOT NULL,
  `ex_sbj` varchar(8) NOT NULL,
  `ex_cnbr` varchar(5) NOT NULL,
  `ex_credits` float NOT NULL,
  `uw_sbj` varchar(8) NOT NULL,
  `uw_cnbr` varchar(5) NOT NULL,
  `uw_credits` float NOT NULL,
  `ticket` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`uw_id`,`institution`,`term_code`,`ex_sbj`,`ex_cnbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transfer_rqmt`
--

DROP TABLE IF EXISTS `transfer_rqmt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transfer_rqmt` (
  `uw_id` int(8) unsigned NOT NULL,
  `institution` varchar(64) NOT NULL,
  `term_code` smallint(4) unsigned NOT NULL,
  `ex_sbj` varchar(8) NOT NULL,
  `ex_cnbr` varchar(5) NOT NULL,
  `rqmt_nbr` int(10) unsigned NOT NULL,
  PRIMARY KEY (`uw_id`,`institution`,`term_code`,`ex_sbj`,`ex_cnbr`,`rqmt_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `upload`
--

DROP TABLE IF EXISTS `upload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `upload` (
  `upload_on` date NOT NULL,
  `uwuserid` varchar(8) NOT NULL,
  `file_name` varchar(256) NOT NULL,
  `type` varchar(20) NOT NULL DEFAULT 'Unknown',
  `note` varchar(255) NOT NULL DEFAULT '',
  `records` int(11) NOT NULL,
  `ticket` int(10) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ticket`)
) ENGINE=MyISAM AUTO_INCREMENT=1623 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_pref`
--

DROP TABLE IF EXISTS `user_pref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_pref` (
  `uwuserid` varchar(8) NOT NULL,
  `name` varchar(20) NOT NULL,
  `value` varchar(20) NOT NULL,
  PRIMARY KEY (`uwuserid`,`name`,`value`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `uwuserid` varchar(8) NOT NULL DEFAULT '',
  `access_level` varchar(16) DEFAULT NULL,
  `uw_id` int(8) unsigned DEFAULT NULL,
  PRIMARY KEY (`uwuserid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-11-12 20:34:35
