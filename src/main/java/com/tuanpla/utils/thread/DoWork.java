/*
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.tuanpla.utils.thread;

/**
 * Simple utility class for watching performance of method invocation.
 *
 * @author uudashr
 *
 */
public class DoWork {

    private long startTime;

    public DoWork() {
        startTime = System.currentTimeMillis();
    }

    public long start() {
        return startTime = System.currentTimeMillis();
    }

    /**
     * Done watching the delay and return the delay between start time to
     * current time.
     *
     * @return the delay between start time to current time in milliseconds
     */
    public long done() {
        return System.currentTimeMillis() - startTime;
    }

    public long doneSecond() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }

    /**
     * Done step working and return time execute a step after restart time for
     * new step
     *
     * @return
     */
    public long doneStep() {
        long result = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();
        return result;
    }

}
