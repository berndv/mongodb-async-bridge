/*
 * Copyright 2015 Bernd Vogt and others.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sourcepit.mongodb.akka;

import java.net.UnknownHostException;
import java.util.concurrent.Executor;

import org.sourcepit.mongodb.async.AsyncDBCollection;
import org.sourcepit.mongodb.async.AsyncDBCursor;
import org.sourcepit.mongodb.async.AsyncMongoClient;

import com.mongodb.MongoClient;

import akka.actor.ActorSystem;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public final class Example
{
   public static void main(String[] args) throws UnknownHostException
   {
      // Create this applications Akka actor system
      final ActorSystem system = ActorSystem.create();

      // Acquire dispatcher that is under control of Akka (Nice, Akkas dispatcher API already implements
      // java.util.concurrent.Executor
      final Executor executor = system.dispatchers().lookup("mongodb-threads");

      // Create async wrapper for MongoDBs standard client (Note: Executor is passed as argument)
      final AsyncMongoClient mongoClient = new AsyncMongoClient(new MongoClient("localhost"), executor, (error) -> {
         system.log().error(error, "Unhandled error in worker thread '{}' of AsyncMongoClient.",
            Thread.currentThread().getName());
      });

      final AsyncDBCollection collection = mongoClient.getDB("foo").getCollection("sweets");

      final AsyncDBCursor cursor = collection.find("{ age: { $gt: 18 } }", "{ name: 1, address: 1 }");

      // handle the actual db call asynchronously
      cursor.forEach(object -> {
         System.out.println(object);
      });
   }

}
