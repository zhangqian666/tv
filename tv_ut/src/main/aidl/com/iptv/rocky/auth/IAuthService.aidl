package com.iptv.rocky.auth;

interface IAuthService
{
	String getEPGSessionId();
	
	String getEPGADDR();
	String getEPGServerIP();
	String getEPGPort();
	
	String getEPGDomain();
	String getEPGDomainBackup();
	String getUserToken();
	String getManagementDomain();
	String getManagementDomainBackup();
	String getUpgradeDomain();
	String getUpgradeDomainBackup();
	String getNTPDomain();
	String getNTPDomainBackup();
	String getEPGGroupNMB();
	String getUserGroupNMB();
	
	String getChannels();
	String getServiceEntries();
	
	void startAuth();
	void startNormalPlatformAuth();
}