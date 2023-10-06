package com.codingapi.springboot.fast.entity;

import com.codingapi.springboot.fast.sort.ISort;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "t_demo")
public class Demo implements ISort {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Integer sort;
}
