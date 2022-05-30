package com.linkpcom.mitrafast.Classes.REST.Models.Requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import okhttp3.MultipartBody;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class ChatFileRequest {
    private String chat_id;
    // 2- image , 3- file , 4- voice
    private String file_type;
    private String reply_key;
    private MultipartBody.Part file;


}