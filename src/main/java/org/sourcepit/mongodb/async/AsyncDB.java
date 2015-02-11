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

import com.mongodb.DB;


/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class AsyncDB
{
   private final Executor executor;
   private final DB db;
   private final Consumer<Throwable> errorHandler;

   public AsyncDB(Executor executor, DB db, Consumer<Throwable> errorHandler)
   {
      this.executor = executor;
      this.db = db;
      this.errorHandler = errorHandler;
   }

   public AsyncDBCollection getCollection(String name)
   {
      return new AsyncDBCollection(executor, db.getCollection(name), errorHandler);
   }

}