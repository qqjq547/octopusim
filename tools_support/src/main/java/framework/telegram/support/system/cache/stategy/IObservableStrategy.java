package framework.telegram.support.system.cache.stategy;


import framework.telegram.support.system.cache.RxCache;
import framework.telegram.support.system.cache.data.CacheResult;

import java.lang.reflect.Type;

import framework.telegram.support.system.cache.RxCache;
import io.reactivex.Observable;


/**
 * author : zchu
 * date   : 2017/10/11
 * desc   :
 */
public interface IObservableStrategy {

    <T> Observable<CacheResult<T>> execute(RxCache rxCache, String key, Observable<T> source, Type type);
}
