package org.brandao.brcache.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TerminalVars 
	extends HashMap<String, Object>{

	private static final long serialVersionUID = 3659583990707468630L;
	
	private TerminalVars parent;
	
	private Map<String, TerminalInfoListener> listeners;
	
	public TerminalVars(){
		this(null, null);
	}

	public TerminalVars(TerminalVars parent, Map<String, Object> defaultValues){
		this.parent    = parent;
		this.listeners = new HashMap<String, TerminalVars.TerminalInfoListener>();

		if(defaultValues != null){
			this.putAll(defaultValues);
		}
	}
	
	public void setListener(String key, TerminalInfoListener value){
		this.listeners.put(key, value);
	}

	public void removeListener(String key){
		this.listeners.remove(key);
	}
	
	@Override
	public synchronized Object put(String key, Object value) {
		
		Object old = super.put(key, value);
		TerminalInfoListener listener = this.listeners.get(key);
		
		if(listener != null){
			listener.actionPerformed(key, old, value, this);
		}
		
		return old;
	}

	public Object set(String key, Object value) {
		return super.put(key, value);
	}
	
	@Override
	public Object get(Object key) {
		Object v = super.get(key);
		return v == null && this.parent != null? this.parent.get(key) : v; 
	}

	@Override
	public boolean containsKey(Object key) {
		boolean k = super.containsKey(key);
		return !k && this.parent != null? this.parent.containsKey(key) : k;
	}

	@Override
	public Set<String> keySet() {
		Set<String> set = new HashSet<String>();
		
		if(this.parent != null){
			set.addAll(this.parent.keySet());
		}
		
		set.addAll(super.keySet());
		
		return set;
	}

	@Override
	public boolean containsValue(Object value) {
		boolean k = super.containsValue(value);
		return !k && this.parent != null? this.parent.containsValue(value) : k;
	}

	public static interface TerminalInfoListener{
		
		void actionPerformed(String key, Object oldValue, Object newValue, TerminalVars terminalInfo);
		
	}
}
