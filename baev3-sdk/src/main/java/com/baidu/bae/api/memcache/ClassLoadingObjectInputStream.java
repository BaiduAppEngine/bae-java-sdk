package com.baidu.bae.api.memcache;

import java.io.IOException;
import java.io.ObjectInputStream;

class ClassLoadingObjectInputStream extends ObjectInputStream
{
    protected ClassLoadingObjectInputStream(java.io.InputStream in) throws IOException
    {
        super(in);
    }

    protected ClassLoadingObjectInputStream () throws IOException
    {
        super();
    }

    @Override
    protected Class<?> resolveClass (java.io.ObjectStreamClass cl) throws IOException, ClassNotFoundException
    {
        try
        {
            return Class.forName(cl.getName(), false, Thread.currentThread().getContextClassLoader());
        }
        catch (ClassNotFoundException e)
        {
            return super.resolveClass(cl);
        }
    }
}

