package monash.ultimateinhaler.service;

import monash.ultimateinhaler.data.Channel;

/**
 * Created by jewel on 8/17/16.
 */
public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);

    void serviceFailure(Exception exception);
}
