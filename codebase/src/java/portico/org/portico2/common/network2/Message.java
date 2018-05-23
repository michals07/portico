/*
 *   Copyright 2018 The Portico Project
 *
 *   This file is part of portico.
 *
 *   portico is free software; you can redistribute it and/or modify
 *   it under the terms of the Common Developer and Distribution License (CDDL) 
 *   as published by Sun Microsystems. For more information see the LICENSE file.
 *   
 *   Use of this software is strictly AT YOUR OWN RISK!!!
 *   If something bad happens you do not have permission to come crying to me.
 *   (that goes for your lawyer as well)
 *
 */
package org.portico2.common.network2;

import org.portico.lrc.utils.MessageHelpers;
import org.portico.utils.messaging.PorticoMessage;
import org.portico2.common.messaging.ResponseMessage;

public class Message
{
	//----------------------------------------------------------
	//                    STATIC VARIABLES
	//----------------------------------------------------------

	//----------------------------------------------------------
	//                   INSTANCE VARIABLES
	//----------------------------------------------------------
	private int requestId;
	
	// serialized version of the message
	private byte[] buffer;
	private Header header;
	
	// cached version of inflated messages
	private PorticoMessage request;

	//----------------------------------------------------------
	//                      CONSTRUCTORS
	//----------------------------------------------------------
	public Message( PorticoMessage request, CallType calltype, int requestId )
	{
		this.request = request;
		this.requestId = requestId;
		
		this.buffer = MessageHelpers.deflate2( request, calltype, requestId );
		this.header = new Header( buffer, 0 );
	}
	
	public Message( byte[] buffer )
	{
		this.buffer = buffer;
		this.header = new Header( buffer, 0 );
		this.requestId = header.getRequestId();
	}

	//----------------------------------------------------------
	//                    INSTANCE METHODS
	//----------------------------------------------------------

	////////////////////////////////////////////////////////////////////////////////////////
	///  Accessors and Mutators   //////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////
	public int getRequestId() { return this.requestId; }
	public Header getHeader() { return this.header; }
	public byte[] getBuffer() { return this.buffer; }
	
	public final PorticoMessage inflateAsPorticoMessage()
	{
		return MessageHelpers.inflate2( buffer, PorticoMessage.class );
	}
	
	public final ResponseMessage inflateAsResponse()
	{
		return MessageHelpers.inflate2( buffer, ResponseMessage.class );
	}
	
	public final void deflateAndStoreResponse( ResponseMessage response )
	{
		if( this.request == null )
			throw new IllegalArgumentException( "You cannot deflate a ResponseMessage without a request" );
		
		this.buffer = MessageHelpers.deflate2( response, this.requestId, this.request );
	}
	
	//----------------------------------------------------------
	//                     STATIC METHODS
	//----------------------------------------------------------
}