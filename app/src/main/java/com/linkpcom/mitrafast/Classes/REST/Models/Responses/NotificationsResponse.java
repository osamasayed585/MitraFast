package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Notification;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotificationsResponse extends BaseResponse {

    private List<Notification> data;

}
