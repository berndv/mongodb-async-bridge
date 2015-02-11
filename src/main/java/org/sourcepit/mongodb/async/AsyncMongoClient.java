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

package org.sourcepit.mongodb.async;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

import com.mongodb.MongoClient;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class AsyncMongoClient
{
   private final MongoClient mongoClient;
   private final Executor executor;
   private final Consumer<Throwable> errorHandler;

   public AsyncMongoClient(MongoClient mongoClient, Executor executor, Consumer<Throwable> errorHandler)
   {
      this.mongoClient = mongoClient;
      this.executor = executor;
      this.errorHandler = errorHandler;
   }

   public AsyncDB getDB(String dbname)
   {
      return new AsyncDB(executor, mongoClient.getDB(dbname), errorHandler);
   }
}
