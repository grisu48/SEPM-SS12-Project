package org.smartsnip.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Exchange object for a code item
 * 
 * @author Felix Niederwanger
 * 
 */
public class XCode implements IsSerializable {
	/** Concrete code */
	public String code;
	/** Concrete code in HTML */
	public String codeHTML;
	/** Code language */
	public String language;
	/** Owner snippet of the code object */
	public Long snippetId;
	/** Version of this code object, auto incrementing */
	public int version;
	/** If the code has a source file attached */
	public boolean downloadAbleSource = false;
	/** Identifier of this code segment */
	public long id = 0L;

}
