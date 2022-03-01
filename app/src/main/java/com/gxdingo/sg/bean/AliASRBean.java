package com.gxdingo.sg.bean;

import java.util.List;

/**
 * @author: Kikis
 * @date: 2021/5/21
 * @page:
 */
public class AliASRBean {

    /**
     * flash_result : {"completed":true,"duration":4260,"latency":167,"sentences":[{"begin_time":450,"channel_id":0,"end_time":2130,"text":"瓜子。"},{"begin_time":450,"channel_id":1,"end_time":2130,"text":"瓜子。"}]}
     * message : SUCCESS
     * result :
     * status : 20000000
     * task_id : 9c30215979b148bcbcb4b2cdec377c93
     */

    private FlashResultBean flash_result;
    private String message;
    private String result;
    private int status;
    private String task_id;

    public FlashResultBean getFlash_result() {
        return flash_result;
    }

    public void setFlash_result(FlashResultBean flash_result) {
        this.flash_result = flash_result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public static class FlashResultBean {
        /**
         * completed : true
         * duration : 4260
         * latency : 167
         * sentences : [{"begin_time":450,"channel_id":0,"end_time":2130,"text":"瓜子。"},{"begin_time":450,"channel_id":1,"end_time":2130,"text":"瓜子。"}]
         */

        private boolean completed;
        private int duration;
        private int latency;
        private List<SentencesBean> sentences;

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getLatency() {
            return latency;
        }

        public void setLatency(int latency) {
            this.latency = latency;
        }

        public List<SentencesBean> getSentences() {
            return sentences;
        }

        public void setSentences(List<SentencesBean> sentences) {
            this.sentences = sentences;
        }

        public static class SentencesBean {
            /**
             * begin_time : 450
             * channel_id : 0
             * end_time : 2130
             * text : 瓜子。
             */

            private int begin_time;
            private int channel_id;
            private int end_time;
            private String text;

            public int getBegin_time() {
                return begin_time;
            }

            public void setBegin_time(int begin_time) {
                this.begin_time = begin_time;
            }

            public int getChannel_id() {
                return channel_id;
            }

            public void setChannel_id(int channel_id) {
                this.channel_id = channel_id;
            }

            public int getEnd_time() {
                return end_time;
            }

            public void setEnd_time(int end_time) {
                this.end_time = end_time;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }
    }
}
