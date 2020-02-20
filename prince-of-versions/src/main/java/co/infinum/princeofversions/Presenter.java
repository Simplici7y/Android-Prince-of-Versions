package co.infinum.princeofversions;

import java.util.concurrent.Executor;

/**
 * This class handles update checks.
 */
interface Presenter {

    /**
     * Start synchronous update check.
     *
     * @param loader    Object for loading data.
     * @param appConfig Configuration of application.
     * @return Update result.
     * @throws Throwable if error happens during check.
     */
    UpdateResult check(Loader loader, ApplicationConfiguration appConfig) throws Throwable;

    /**
     * Start asynchronous update check.
     *
     * @param loader    Object for loading data.
     * @param executor  Object for executing update check.
     * @param callback  Callback to which result will be notified.
     * @param appConfig Application configuration.
     * @return Call object which offers method for cancelling update check.
     */
    PrinceOfVersionsCancelable check(Loader loader, Executor executor, UpdaterCallback callback, ApplicationConfiguration appConfig);

}
