package com.linkpcom.mitrafast.Classes.REST.Models.Responses;


import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Category;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoriesResponse extends BaseResponse {
    private List<Category> data;
}
