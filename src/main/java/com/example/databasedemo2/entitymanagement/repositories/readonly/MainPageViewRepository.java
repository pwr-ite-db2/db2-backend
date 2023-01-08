package com.example.databasedemo2.entitymanagement.repositories.readonly;

import com.example.databasedemo2.entitymanagement.views.MainPageView;

import java.util.Date;
import java.util.List;

public interface MainPageViewRepository extends BaseReadOnlyRepository<MainPageView, Integer> {
    List<MainPageView> findAllByReleaseDateAfter(Date date);
}