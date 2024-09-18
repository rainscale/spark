package com.godlin.demo.service.impl;

import com.godlin.demo.entity.Contact;
import com.godlin.demo.mapper.ContactMapper;
import com.godlin.demo.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactMapper contactMapper;

    @Override
    public List<Contact> listAll() {
        return contactMapper.listAll();
    }
}
