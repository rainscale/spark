package com.godlin.demo.controller;

import com.godlin.demo.api.CommonPage;
import com.godlin.demo.api.CommonResult;
import com.godlin.demo.entity.Contact;
import com.godlin.demo.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/contact")
public class ContactController {
    @Autowired
    private ContactService contactService;

    @GetMapping("/listAll")
    @ResponseBody
    public CommonResult<CommonPage<Contact>> getList() {
        List<Contact> contactList = contactService.listAll();
        return CommonResult.success(CommonPage.restPage(contactList), "获取表格数据成功");
    }
}
