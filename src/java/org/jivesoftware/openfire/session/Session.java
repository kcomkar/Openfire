/**
 * $RCSfile$
 * $Revision: 3174 $
 * $Date: 2005-12-08 17:41:00 -0300 (Thu, 08 Dec 2005) $
 *
 * Copyright (C) 2007 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution.
 */

package org.jivesoftware.openfire.session;

import org.jivesoftware.openfire.RoutableChannelHandler;
import org.jivesoftware.openfire.StreamID;
import org.xmpp.packet.JID;
import org.xmpp.packet.Packet;

import java.net.UnknownHostException;
import java.util.Date;

/**
 * The session represents a connection between the server and a client (c2s) or
 * another server (s2s) as well as a connection with a component. Authentication and
 * user accounts are associated with c2s connections while s2s has an optional authentication
 * association but no single user user.<p>
 *
 * Obtain object managers from the session in order to access server resources.
 *
 * @author Gaston Dombiak
 */
public interface Session extends RoutableChannelHandler {

    /**
     * Version of the XMPP spec supported as MAJOR_VERSION.MINOR_VERSION (e.g. 1.0).
     */
	public static final int MAJOR_VERSION = 1;
    public static final int MINOR_VERSION = 0;

    public static final int STATUS_CLOSED = -1;
    public static final int STATUS_CONNECTED = 1;
    public static final int STATUS_AUTHENTICATED = 3;

    /**
      * Obtain the address of the user. The address is used by services like the core
      * server packet router to determine if a packet should be sent to the handler.
      * Handlers that are working on behalf of the server should use the generic server
      * hostname address (e.g. server.com).
      *
      * @return the address of the packet handler.
      */
    public JID getAddress();

    /**
     * Sets the new address of this session. The address is used by services like the core
     * server packet router to determine if a packet should be sent to the handler.
     * Handlers that are working on behalf of the server should use the generic server
     * hostname address (e.g. server.com).
     *
     * @param address the new address of this session.
     */
    public void setAddress(JID address);

    /**
     * Obtain the current status of this session.
     *
     * @return The status code for this session
     */
    public int getStatus();

    /**
     * Obtain the stream ID associated with this sesison. Stream ID's are generated by the server
     * and should be unique and random.
     *
     * @return This session's assigned stream ID
     */
    public StreamID getStreamID();

    /**
     * Obtain the name of the server this session belongs to.
     *
     * @return the server name.
     */
    public String getServerName();

    /**
     * Obtain the date the session was created.
     *
     * @return the session's creation date.
     */
    public Date getCreationDate();

    /**
     * Obtain the time the session last had activity.
     *
     * @return The last time the session received activity.
     */
    public Date getLastActiveDate();

    /**
     * Obtain the number of packets sent from the client to the server.
     *
     * @return The number of packets sent from the client to the server.
     */
    public long getNumClientPackets();

    /**
     * Obtain the number of packets sent from the server to the client.
     *
     * @return The number of packets sent from the server to the client.
     */
    public long getNumServerPackets();
    
    /**
     * Close this session including associated socket connection. The order of
     * events for closing the session is:
     * <ul>
     *      <li>Set closing flag to prevent redundant shutdowns.
     *      <li>Call notifyEvent all listeners that the channel is shutting down.
     *      <li>Close the socket.
     * </ul>
     */
    public void close();

    /**
     * Returns true if the connection/session is closed.
     *
     * @return true if the connection is closed.
     */
    public boolean isClosed();

    /**
     * Returns true if this connection is secure.
     *
     * @return true if the connection is secure (e.g. SSL/TLS)
     */
    public boolean isSecure();

    /**
     * Returns the IP address string in textual presentation.
     *
     * @return  the raw IP address in a string format.
     * @throws java.net.UnknownHostException if IP address of host could not be determined.
     */
    public String getHostAddress() throws UnknownHostException;

    /**
     * Gets the host name for this IP address.
     *
     * <p>If this InetAddress was created with a host name,
     * this host name will be remembered and returned;
     * otherwise, a reverse name lookup will be performed
     * and the result will be returned based on the system
     * configured name lookup service. If a lookup of the name service
     * is required, call
     * {@link java.net.InetAddress#getCanonicalHostName() getCanonicalHostName}.
     *
     * <p>If there is a security manager, its
     * <code>checkConnect</code> method is first called
     * with the hostname and <code>-1</code>
     * as its arguments to see if the operation is allowed.
     * If the operation is not allowed, it will return
     * the textual representation of the IP address.
     *
     * @return  the host name for this IP address, or if the operation
     *    is not allowed by the security check, the textual
     *    representation of the IP address.
     * @throws java.net.UnknownHostException if IP address of host could not be determined.
     *
     * @see java.net.InetAddress#getCanonicalHostName
     * @see SecurityManager#checkConnect
     */
    public String getHostName() throws UnknownHostException;

    public abstract void process(Packet packet);

    /**
     * Delivers raw text to this connection. This is a very low level way for sending
     * XML stanzas to the client. This method should not be used unless you have very
     * good reasons for not using {@link #process(Packet)}.<p>
     *
     * This method avoids having to get the writer of this connection and mess directly
     * with the writer. Therefore, this method ensures a correct delivery of the stanza
     * even if other threads were sending data concurrently.
     *
     * @param text the XML stanzas represented kept in a String.
     */
    public void deliverRawText(String text);

    /**
     * Verifies that the connection is still live. Typically this is done by
     * sending a whitespace character between packets.
     *
     * // TODO No one is sending this message now. Delete it?
     *
     * @return true if the socket remains valid, false otherwise.
     */
    public boolean validate();
}