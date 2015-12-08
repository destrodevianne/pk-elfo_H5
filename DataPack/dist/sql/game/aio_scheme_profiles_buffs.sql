DROP TABLE IF EXISTS `aio_scheme_profiles_buffs`;
CREATE TABLE IF NOT EXISTS `aio_scheme_profiles_buffs` (
  `charId` int(10) unsigned NOT NULL,
  `profile` varchar(45) DEFAULT NULL,
  `buff_id` varchar(45) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
