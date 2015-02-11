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

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class AsyncDBCursor implements AutoCloseable
{
   private final Executor executor;
   private final DBCursor cursor;
   private final Consumer<Throwable> errorHandler;

   public AsyncDBCursor(Executor executor, DBCursor cursor, Consumer<Throwable> errorHandler)
   {
      this.executor = executor;
      this.cursor = cursor;
      this.errorHandler = errorHandler;
   }

   @Override
   public void close()
   {
      close(null);
   }

   public void close(Consumer<Throwable> error)
   {
      async(() -> {
         cursor.close();
      }, error);
   }


   public void forEach(Consumer<DBObject> handler)
   {
      forEach(handler, null);
   }

   public void forEach(Consumer<DBObject> handler, Consumer<Throwable> error)
   {
      async(() -> {
         while (cursor.hasNext())
         {
            handler.accept(cursor.next());
         }
      }, error);
   }

   public void one(Consumer<DBObject> handler)
   {
      one(handler, null);
   }

   public void one(Consumer<DBObject> handler, Consumer<Throwable> error)
   {
      async(() -> {
         handler.accept(cursor.one());
      }, error);
   }

   public void hasNext(Consumer<Boolean> handler)
   {
      hasNext(handler, null);
   }

   public void hasNext(Consumer<Boolean> handler, Consumer<Throwable> error)
   {
      async(() -> {
         handler.accept(Boolean.valueOf(cursor.hasNext()));
      }, error);
   }

   public void next(Consumer<DBObject> handler)
   {
      next(handler, null);
   }

   public void next(Consumer<DBObject> handler, Consumer<Throwable> error)
   {
      async(() -> {
         handler.accept(cursor.next());
      }, error);
   }

   private void async(Runnable runnable, Consumer<Throwable> error)
   {
      executor.execute(() -> {
         try
         {
            runnable.run();
         }
         catch (Error | RuntimeException e)
         {
            final Consumer<Throwable> handler = error == null ? errorHandler : error;
            if (handler == null)
            {
               throw e;
            }
            else
            {
               handler.accept(e);
            }
         }
      });
   }
}