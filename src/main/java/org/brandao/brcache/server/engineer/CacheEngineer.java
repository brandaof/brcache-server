package org.brandao.brcache.server.engineer;

import java.io.InputStream;

import org.brandao.brcache.RecoverException;
import org.brandao.brcache.StorageException;

public interface CacheEngineer {

	/* put */
	
    boolean put(CacheMetaData cache, String key, 
    		InputStream inputData, long creationTime, long mostRecentTime) throws StorageException;

    boolean put(CacheMetaData cache, String key, 
    		byte[] inputData, long creationTime, long mostRecentTime) throws StorageException;

    boolean put(CacheMetaData cache, String key, 
    		ColumnDataType[] cols, Object[] values, long creationTime, long mostRecentTime) throws StorageException;
    
    /* replace */
    
    boolean replace(CacheMetaData cache, String oldKey, String newKey, 
    		InputStream inputData) throws StorageException;

    boolean replace(CacheMetaData cache, String oldKey, String oldUID, String newKey, String newUID, 
    		byte[] inputData) throws StorageException;

    boolean replace(CacheMetaData cache, String oldKey, String oldUID, String newKey, String newUID, 
    		ColumnDataType[] cols, Object[] values) throws StorageException;
    
    /* set */
    
    boolean set(CacheMetaData cache, String oldKey, String oldUID, String newKey, String newUID, 
    		InputStream inputData) throws StorageException;

    boolean set(CacheMetaData cache, String oldKey, String oldUID, String newKey, String newUID, 
    		byte[] inputData) throws StorageException;

    boolean set(CacheMetaData cache, String oldKey, String oldUID, String newKey, String newUID, 
    		ColumnDataType[] cols, Object[] values) throws StorageException;
    
    /* putIfAbsent */
    
    InputStream putIfAbsent(CacheMetaData cache, String oldKey, String oldUID, String newKey, String newUID,
    		InputStream inputData) throws StorageException;

    InputStream putIfAbsent(CacheMetaData cache, String oldKey, String oldUID, String newKey, String newUID,
    		byte[] inputData) throws StorageException;

    Object[] putIfAbsent(CacheMetaData cache, String oldKey, String oldUID, String newKey, String newUID,
    		ColumnDataType[] cols, Object[] values) throws StorageException;
    
    /* get */
    
    Object[] get(CacheMetaData cache, String key, String uid, ColumnDataType[] cols) throws RecoverException;

    Object[] get(CacheMetaData cache, String key, String uid, ColumnDataType[] cols, boolean lock) throws RecoverException;

    InputStream get(CacheMetaData cache, String key, String uid) throws RecoverException;

    InputStream get(CacheMetaData cache, String key, String uid, boolean lock) throws RecoverException;
    
    /* remove */
    
    boolean remove(CacheMetaData cache, String key, String uid) throws StorageException;
    
    /* contains */
    
    boolean containsKey(CacheMetaData cache, String key, String uid) throws RecoverException;

    boolean containsKey(CacheMetaData cache, String key, String uid, boolean lock) throws RecoverException;
    
    /* check */
    
    boolean isDeleteOnExit();

	void setDeleteOnExit(boolean deleteOnExit);

	void clear();
	
	void destroy();
	
}
