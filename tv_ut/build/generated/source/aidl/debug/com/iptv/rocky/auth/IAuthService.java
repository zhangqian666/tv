/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /studio_tv/tv/tv_ut/src/main/aidl/com/iptv/rocky/auth/IAuthService.aidl
 */
package com.iptv.rocky.auth;
public interface IAuthService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.iptv.rocky.auth.IAuthService
{
private static final java.lang.String DESCRIPTOR = "com.iptv.rocky.auth.IAuthService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.iptv.rocky.auth.IAuthService interface,
 * generating a proxy if needed.
 */
public static com.iptv.rocky.auth.IAuthService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.iptv.rocky.auth.IAuthService))) {
return ((com.iptv.rocky.auth.IAuthService)iin);
}
return new com.iptv.rocky.auth.IAuthService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getEPGSessionId:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getEPGSessionId();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getEPGADDR:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getEPGADDR();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getEPGServerIP:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getEPGServerIP();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getEPGPort:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getEPGPort();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getEPGDomain:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getEPGDomain();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getEPGDomainBackup:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getEPGDomainBackup();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getUserToken:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getUserToken();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getManagementDomain:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getManagementDomain();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getManagementDomainBackup:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getManagementDomainBackup();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getUpgradeDomain:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getUpgradeDomain();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getUpgradeDomainBackup:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getUpgradeDomainBackup();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getNTPDomain:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getNTPDomain();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getNTPDomainBackup:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getNTPDomainBackup();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getEPGGroupNMB:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getEPGGroupNMB();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getUserGroupNMB:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getUserGroupNMB();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getChannels:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getChannels();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getServiceEntries:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getServiceEntries();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_startAuth:
{
data.enforceInterface(DESCRIPTOR);
this.startAuth();
reply.writeNoException();
return true;
}
case TRANSACTION_startNormalPlatformAuth:
{
data.enforceInterface(DESCRIPTOR);
this.startNormalPlatformAuth();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.iptv.rocky.auth.IAuthService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public java.lang.String getEPGSessionId() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getEPGSessionId, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getEPGADDR() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getEPGADDR, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getEPGServerIP() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getEPGServerIP, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getEPGPort() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getEPGPort, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getEPGDomain() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getEPGDomain, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getEPGDomainBackup() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getEPGDomainBackup, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getUserToken() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getUserToken, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getManagementDomain() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getManagementDomain, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getManagementDomainBackup() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getManagementDomainBackup, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getUpgradeDomain() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getUpgradeDomain, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getUpgradeDomainBackup() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getUpgradeDomainBackup, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getNTPDomain() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getNTPDomain, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getNTPDomainBackup() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getNTPDomainBackup, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getEPGGroupNMB() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getEPGGroupNMB, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getUserGroupNMB() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getUserGroupNMB, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getChannels() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getChannels, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getServiceEntries() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getServiceEntries, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void startAuth() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startAuth, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void startNormalPlatformAuth() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startNormalPlatformAuth, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_getEPGSessionId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getEPGADDR = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getEPGServerIP = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getEPGPort = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getEPGDomain = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getEPGDomainBackup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_getUserToken = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_getManagementDomain = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_getManagementDomainBackup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_getUpgradeDomain = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_getUpgradeDomainBackup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_getNTPDomain = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_getNTPDomainBackup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_getEPGGroupNMB = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_getUserGroupNMB = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_getChannels = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_getServiceEntries = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_startAuth = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_startNormalPlatformAuth = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
}
public java.lang.String getEPGSessionId() throws android.os.RemoteException;
public java.lang.String getEPGADDR() throws android.os.RemoteException;
public java.lang.String getEPGServerIP() throws android.os.RemoteException;
public java.lang.String getEPGPort() throws android.os.RemoteException;
public java.lang.String getEPGDomain() throws android.os.RemoteException;
public java.lang.String getEPGDomainBackup() throws android.os.RemoteException;
public java.lang.String getUserToken() throws android.os.RemoteException;
public java.lang.String getManagementDomain() throws android.os.RemoteException;
public java.lang.String getManagementDomainBackup() throws android.os.RemoteException;
public java.lang.String getUpgradeDomain() throws android.os.RemoteException;
public java.lang.String getUpgradeDomainBackup() throws android.os.RemoteException;
public java.lang.String getNTPDomain() throws android.os.RemoteException;
public java.lang.String getNTPDomainBackup() throws android.os.RemoteException;
public java.lang.String getEPGGroupNMB() throws android.os.RemoteException;
public java.lang.String getUserGroupNMB() throws android.os.RemoteException;
public java.lang.String getChannels() throws android.os.RemoteException;
public java.lang.String getServiceEntries() throws android.os.RemoteException;
public void startAuth() throws android.os.RemoteException;
public void startNormalPlatformAuth() throws android.os.RemoteException;
}
