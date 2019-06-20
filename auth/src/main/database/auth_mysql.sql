
INSERT INTO ofVersion (name, version) VALUES ('auth', 0);

CREATE TABLE hdUserProperty (
userName varchar(64) NOT NULL,
zoneId int(11) DEFAULT NULL,
zoneName varchar(100) DEFAULT NULL,
hotelId int(11) DEFAULT NULL,
hotelName varchar(100) DEFAULT NULL,
roomNum varchar(50) DEFAULT NULL,
type varchar(30) NOT NULL,
roomAmount int(11) DEFAULT NULL,
creationDate timestamp NULL DEFAULT NULL,
modificationDamodificationDate timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (userName)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE hdUserRoom (
id bigint(20) NOT NULL AUTO_INCREMENT,
zoneId int(11) NOT NULL,
userName varchar(64) NOT NULL,
roomName varchar(50) NOT NULL,
motifyDate timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (id),
KEY hdUserRoom_userName_idx (userName)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;