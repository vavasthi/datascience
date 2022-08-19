package com.avasthi.datascience.caching.pojos;

import java.io.Serializable;

public class KeyPrefixForCache implements Serializable {

    private String prefix;
    private Object key;

    public KeyPrefixForCache(final String prefix, final Object key) {
        this.prefix = prefix;
        this.key = key;
    }

    public KeyPrefixForCache() {
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(final Object key) {
        this.key = key;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyPrefixForCache)) return false;

        final KeyPrefixForCache that = (KeyPrefixForCache) o;

        if (prefix != null ? !prefix.equals(that.prefix) : that.prefix != null) return false;
        return key != null ? key.equals(that.key) : that.key == null;

    }

    @Override
    public int hashCode() {
        int result = prefix != null ? prefix.hashCode() : 0;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CacheKeyPrefix{" +
                "prefix='" + prefix + '\'' +
                ", key=" + key+
                '}';
    }
}