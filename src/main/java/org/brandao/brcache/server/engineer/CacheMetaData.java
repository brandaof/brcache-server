package org.brandao.brcache.server.engineer;

import java.util.Map;

import org.brandao.entityfilemanager.EntityFile;

public class CacheMetaData {

	private int id;
	
	private String schema;
	
	private String name;
	
	private String key;

	private int indexColumnKey;
	
	private CacheType type;
	
	private ColumnDataType[] columns;

	private String[] columnsName;
	
	private Map<String,ColumnDataType> mappedColumns;
	
	private boolean transaction;
	
	private long timeToLive;
	
	private long timeToIdle;
	
	private long maxDataSize;
	
	private int maxEntries;

	private CacheEngineer cacheEngineer;
	
	private EntityFile<CacheMetaData> fileMetadata;
	
	private EntityFile<byte[]> fileData;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndexColumnKey() {
		return indexColumnKey;
	}

	public void setIndexColumnKey(int indexColumnKey) {
		this.indexColumnKey = indexColumnKey;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public CacheType getType() {
		return type;
	}

	public void setType(CacheType type) {
		this.type = type;
	}

	public ColumnDataType[] getColumns() {
		return columns;
	}

	public void setColumns(ColumnDataType[] columns) {
		this.columns = columns;
	}

	public Map<String, ColumnDataType> getMappedColumns() {
		return mappedColumns;
	}

	public void setMappedColumns(Map<String, ColumnDataType> mappedColumns) {
		this.mappedColumns = mappedColumns;
	}

	public String[] getColumnsName() {
		return columnsName;
	}

	public void setColumnsName(String[] columnsName) {
		this.columnsName = columnsName;
	}

	public boolean isTransaction() {
		return transaction;
	}

	public void setTransaction(boolean transaction) {
		this.transaction = transaction;
	}

	public long getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

	public long getTimeToIdle() {
		return timeToIdle;
	}

	public void setTimeToIdle(long timeToIdle) {
		this.timeToIdle = timeToIdle;
	}

	public long getMaxDataSize() {
		return maxDataSize;
	}

	public void setMaxDataSize(long maxDataSize) {
		this.maxDataSize = maxDataSize;
	}

	public int getMaxEntries() {
		return maxEntries;
	}

	public void setMaxEntries(int maxEntries) {
		this.maxEntries = maxEntries;
	}

	public CacheEngineer getCacheEngineer() {
		return cacheEngineer;
	}

	public void setCacheEngineer(CacheEngineer cacheEngineer) {
		this.cacheEngineer = cacheEngineer;
	}

	public EntityFile<CacheMetaData> getFileMetadata() {
		return fileMetadata;
	}

	public void setFileMetadata(EntityFile<CacheMetaData> fileMetadata) {
		this.fileMetadata = fileMetadata;
	}

	public EntityFile<byte[]> getFileData() {
		return fileData;
	}

	public void setFileData(EntityFile<byte[]> fileData) {
		this.fileData = fileData;
	}
	
}
