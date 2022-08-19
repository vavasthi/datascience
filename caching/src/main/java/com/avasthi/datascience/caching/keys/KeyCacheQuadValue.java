package com.avasthi.datascience.caching.keys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyCacheQuadValue  implements Serializable {
    private Object key1;
    private Object key2;
    private Object key3;
    private Object key4;
}
