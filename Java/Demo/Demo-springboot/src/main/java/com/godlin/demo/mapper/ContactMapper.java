package com.godlin.demo.mapper;

import com.godlin.demo.entity.Contact;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContactMapper {
    List<Contact> listAll();
}