package org.brandao.brcache.server.engineer;

import org.brandao.brcache.imp.persistence.DataInputStream;
import org.brandao.brcache.imp.persistence.DataOutputStream;

public class ColumnDataType {

	private int index;
	
	private String name;
	
	private ColumnTypes type;
	
	private boolean key;
	
	private byte precision;
	
	private byte scale;
	
	private int length;
	
	private boolean notNull;
	
	private boolean autoIncrement;
	
	private Object defaultValue;
	
	private String sequence;
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public ColumnTypes getType() {
		return type;
	}

	public void setType(ColumnTypes type) {
		this.type = type;
	}

	public boolean isKey() {
		return key;
	}

	public void setKey(boolean key) {
		this.key = key;
	}

	public boolean isNotNull() {
		return notNull;
	}

	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}


	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	
	public byte getPrecision() {
		return precision;
	}

	public void setPrecision(byte precision) {
		this.precision = precision;
	}

	public byte getScale() {
		return scale;
	}

	public void setScale(byte scale) {
		this.scale = scale;
	}

	public void write(DataOutputStream out, Object value){
	}

	public Object read(DataInputStream in){
		return null;
	}
	
	public boolean isValid(Object value){
		return false;
	}
	
}
