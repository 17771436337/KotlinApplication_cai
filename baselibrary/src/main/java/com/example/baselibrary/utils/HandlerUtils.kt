package com.example.baselibrary.utils

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.util.SparseArray
import androidx.annotation.NonNull
import java.util.*

class HandlerUtils private constructor(): Handler.Callback {

    private val TAG = HandlerUtils::class.simpleName
    private var DEBUG :Boolean= true
    private val mHandler = Handler(Looper.getMainLooper(), this)
    private val mMessage = UiMessage(null)
    private val mListenersSpecific = SparseArray<MutableList<UiMessageCallback>>()
    private val mListenersUniversal: MutableList<UiMessageCallback> = ArrayList()
    private val mDefensiveCopyList: MutableList<UiMessageCallback> = ArrayList()


    companion object{
       private val INSTANCE = HandlerUtils()

        fun getInstance(): HandlerUtils {
            return INSTANCE
        }
    }


    override fun handleMessage(msg: Message): Boolean {
        mMessage.setMessage(msg)
        if (DEBUG) {
            logMessageHandling(mMessage)
        }

        // process listeners for specified type of message what
        synchronized(mListenersSpecific) {
            val idListeners: List<UiMessageCallback>? =
                mListenersSpecific[msg.what]
            if (idListeners != null) {
                if (idListeners.size == 0) {
                    mListenersSpecific.remove(msg.what)
                } else {
                    mDefensiveCopyList.addAll(idListeners)
                    for (callback in mDefensiveCopyList) {
                        callback.handleMessage(mMessage)
                    }
                    mDefensiveCopyList.clear()
                }
            }
        }

        // process universal listeners
        synchronized(mListenersUniversal) {
            if (mListenersUniversal.size > 0) {
                mDefensiveCopyList.addAll(mListenersUniversal)
                for (callback in mDefensiveCopyList) {
                    callback.handleMessage(mMessage)
                }
                mDefensiveCopyList.clear()
            }
        }
        mMessage.setMessage(null)
        return true
    }


    /**
     * Sends an empty Message containing only the message ID.
     *
     * @param id The message ID.
     */
    fun send(id: Int) {
        mHandler.sendEmptyMessage(id)
    }

    /**
     * Sends an empty Message containing only the message ID.
     *
     * @param id The message ID.
     * @param delayMillis The message delayMillis
     */
    fun sendDelayed(id: Int, delayMillis: Long) {
        mHandler.sendEmptyMessageDelayed(id, delayMillis)
    }


    /**
     * Sends a message containing the ID and an arbitrary object.
     *
     * @param id  The message ID.
     * @param obj The object.
     */
    fun send(id: Int, @NonNull obj: Any?) {
        mHandler.sendMessage(mHandler.obtainMessage(id, obj))
    }

    /**
     * Add listener for specific type of message by its ID.
     * Don't forget to call [.removeListener] or
     * [.removeListeners]
     *
     * @param key       The ID of message that will be only notified to listener.
     * @param listener The listener.
     */
    fun addListener(key: Int, @NonNull listener: UiMessageCallback) {
        synchronized(mListenersSpecific) {
            var idListeners =
                mListenersSpecific[key]
            if (idListeners == null) {
                idListeners = ArrayList()
                mListenersSpecific.put(key, idListeners)
            }
            if (!idListeners.contains(listener)) {
                idListeners.add(listener)
            }
        }
    }

    /**
     * Add listener for all messages.
     *
     * @param listener The listener.
     */
    fun addListener(@NonNull listener: UiMessageCallback) {
        synchronized(mListenersUniversal) {
            if (!mListenersUniversal.contains(listener)) {
                mListenersUniversal.add(listener)
            } else {
                if (DEBUG) {
                    Log.w(TAG, "Listener is already added. $listener")
                }else{


                }
            }
        }
    }

    /**
     * Remove listener for all messages.
     *
     * @param listener The listener to remove.
     */
    fun removeListener(@NonNull listener: UiMessageCallback) {
        synchronized(mListenersUniversal) {
            if (DEBUG && !mListenersUniversal.contains(listener)) {
                Log.w(
                    TAG,
                    "Trying to remove a listener that is not registered. $listener"
                )
            }
            mListenersUniversal.remove(listener)
        }
    }

    /**
     * Remove all listeners for desired message ID.
     *
     * @param key The id of the message to stop listening to.
     */
    fun removeListeners(key: Int) {
        if (DEBUG) {
            val callbacks: List<UiMessageCallback>? = mListenersSpecific[key]
            if (callbacks == null || callbacks.size == 0) {
                Log.w(
                    TAG,
                    "Trying to remove specific listeners that are not registered. ID $key"
                )
            }
        }
        synchronized(mListenersSpecific) { mListenersSpecific.delete(key) }
    }

    /**
     * Remove the specific listener for desired message ID.
     *
     * @param key       The id of the message to stop listening to.
     * @param listener The listener which should be removed.
     */
    fun removeListener(key: Int, @NonNull listener: UiMessageCallback) {
        synchronized(mListenersSpecific) {
            val callbacks =
                mListenersSpecific[key]
            if (callbacks != null && !callbacks.isEmpty()) {
                if (DEBUG) {
                    if (!callbacks.contains(listener)) {
                        Log.w(
                            TAG,
                            "Trying to remove specific listener that is not registered. ID $key, $listener"
                        )
                        return
                    }
                }
                callbacks.remove(listener)
            } else {
                if (DEBUG) {
                    Log.w(
                        TAG,
                        "Trying to remove specific listener that is not registered. ID $key, $listener"
                    )
                }else{

                }
            }
        }
    }

    private fun logMessageHandling(@NonNull msg: UiMessage) {
        val idListeners: List<UiMessageCallback>? = mListenersSpecific[msg.id]
        if ((idListeners == null || idListeners.size == 0) && mListenersUniversal.size == 0) {
            Log.w(
                TAG,
                "Delivering FAILED for message ID " + msg.id + ". No listeners. " + msg.toString()
            )
        } else {
            val stringBuilder = StringBuilder()
            stringBuilder.append("Delivering message ID ")
            stringBuilder.append(msg.id)
            stringBuilder.append(", Specific listeners: ")
            if (idListeners == null || idListeners.size == 0) {
                stringBuilder.append(0)
            } else {
                stringBuilder.append(idListeners.size)
                stringBuilder.append(" [")
                for (i in idListeners.indices) {
                    stringBuilder.append(idListeners[i].javaClass.simpleName)
                    if (i < idListeners.size - 1) {
                        stringBuilder.append(",")
                    }
                }
                stringBuilder.append("]")
            }
            stringBuilder.append(", Universal listeners: ")
            synchronized(mListenersUniversal) {
                if (mListenersUniversal.size == 0) {
                    stringBuilder.append(0)
                } else {
                    stringBuilder.append(mListenersUniversal.size)
                    stringBuilder.append(" [")
                    for (i in mListenersUniversal.indices) {
                        stringBuilder.append(mListenersUniversal[i].javaClass.simpleName)
                        if (i < mListenersUniversal.size - 1) {
                            stringBuilder.append(",")
                        }
                    }
                    stringBuilder.append("], Message: ")
                }
            }
            stringBuilder.append(msg.toString())
            Log.v(TAG, stringBuilder.toString())
        }
    }


    public class UiMessage(private var mMessage: Message?) {
        fun setMessage(message: Message?) {
            mMessage = message
        }

         val id: Int get() = mMessage!!.what
        val obj: Any get() = mMessage!!.obj

        override fun toString(): String {
            return "{ " +
                    "id=" + id +
                    ", obj=" + obj +
                    " }"
        }
    }

    interface UiMessageCallback {
        fun handleMessage(@NonNull localMessage: UiMessage?)
    }





}