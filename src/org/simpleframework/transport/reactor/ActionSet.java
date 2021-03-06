/*
 * ActionSet.java February 2013
 *
 * Copyright (C) 2013, Niall Gallagher <niallg@users.sf.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.simpleframework.transport.reactor;

import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_CONNECT;
import static java.nio.channels.SelectionKey.OP_READ;
import static java.nio.channels.SelectionKey.OP_WRITE;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * The <code>ActionSet</code> object represents a set of actions that
 * are associated with a particular selection key. Here the set
 * stores an <code>Action</code> for each of the interested operation
 * types. In some situations a single action may be interested in
 * several operations which must be remembered by the set.
 * 
 * @author Niall Gallagher
 */
class ActionSet implements Iterable<Action> {
   
   /**
    * This is the selection key associated with the action set.
    */
   private final SelectionKey key;
   
   /**
    * This contains the the actions indexed by operation type.
    */
   private final Action[] set;
   
   /**
    * Constructor for the <code>ActionSet</code> object. This is
    * used to create a set for storing actions keyed by operation
    * type. Only one action is kept per operation type.
    * 
    * @param key this is the associated selection key
    */
   public ActionSet(SelectionKey key) {
      this.set = new Action[4];
      this.key = key;
   }   
   
   /**
    * This provides the selection key associated with the action set.
    * For each ready operation on the selection key the set contains
    * an action that can be executed. 
    * 
    * @return this provides the selection key for this action set
    */
   public SelectionKey key() {
      return key;
   }
   
   /**
    * This provides the channel associated with the action set. This
    * is the channel that is registered for selection using the
    * interested operations for the set.
    * 
    * @return this returns the selectable channel for the action set
    */
   public SelectableChannel channel() {
      return key.channel();
   }
  
   /**
    * This provides an iterator of the actions that exist within the
    * action set. Regardless of whether a single action is interested
    * is several operations this will return an iteration of unique
    * actions. Modifications to the iterator do not affect the set.
    * 
    * @return this returns an iterator of unique actions for the set
    */
   public Iterator<Action> iterator() {
      Set<Action> actions = new HashSet<Action>();
      
      for(Action action : set) {
         if(action != null) {
            actions.add(action);
         }
      }
      return actions.iterator();
   }
   
   /**
    * This is used to attach an action to the set for a specific
    * interest bitmask. If the bitmask contains several operations
    * then the action is registered for each individual operation.
    * 
    * @param action this is the action that is to be attached
    * @param interest this is the interest for the action
    */
   public void attach(Action action, int interest) {
      if((interest | OP_READ) == OP_READ) {
         set[0] = action;
      }
      if((interest | OP_WRITE) == OP_WRITE) {
         set[1] = action;       
      }
      if((interest | OP_ACCEPT) == OP_ACCEPT) {
         set[2] = action;        
      }
      if((interest | OP_CONNECT) == OP_CONNECT) {
         set[3] = action;
      }
   }
   
   /**
    * This is used to remove interest from the set. Removal of 
    * interest from the set is performed by registering a null for
    * the interest operation.
    * 
    * @param interest this is the interest to be removed 
    */
   public void remove(int interest) {
      if((interest | OP_READ) == OP_READ) {
         set[0] = null;         
      }
      if((interest | OP_WRITE) == OP_WRITE) {
         set[1] = null;         
      }
      if((interest | OP_ACCEPT) == OP_ACCEPT) {
         set[2] = null;         
      }
      if((interest | OP_CONNECT) == OP_CONNECT) {
         set[3] = null;         
      }
   }
   
   /**
    * This is used to clear all interest from the set. This will
    * basically clear out any actions that have been registered with
    * the set. After invocation the iterator will be empty.
    */
   public void clear() {
      set[0] = set[1] = 
      set[2] = set[3] = null;
   }
   
   /**
    * This is used to cancel the <code>SelectionKey</code> associated
    * with the action set. Canceling the key in this manner ensures
    * it is not returned in further selection operations. 
    */
   public void cancel() {
      key.cancel();
   }
}
