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

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class AsyncDBCollection
{
   private final Executor executor;
   private final DBCollection collection;
   private final Consumer<Throwable> errorHandler;

   public AsyncDBCollection(Executor executor, DBCollection collection, Consumer<Throwable> errorHandler)
   {
      this.executor = executor;
      this.collection = collection;
      this.errorHandler = errorHandler;
   }

   public AsyncDBCursor find(String ref)
   {
      return find(fromString(ref));
   }

   public AsyncDBCursor find(DBObject ref)
   {
      return new AsyncDBCursor(executor, collection.find(ref), errorHandler);
   }

   public AsyncDBCursor find(String ref, String keys)
   {
      return find(fromString(ref), fromString(keys));
   }

   public AsyncDBCursor find(DBObject ref, DBObject keys)
   {
      return new AsyncDBCursor(executor, collection.find(ref, keys), errorHandler);
   }

   private static DBObject fromString(String ref)
   {
      return (DBObject) JSON.parse(ref);
   }
}