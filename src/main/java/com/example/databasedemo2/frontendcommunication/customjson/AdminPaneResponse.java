package com.example.databasedemo2.frontendcommunication.customjson;

import com.example.databasedemo2.entitymanagement.views.ArticleInEditView;
import com.example.databasedemo2.entitymanagement.views.ArticleInMakingView;
import com.example.databasedemo2.entitymanagement.views.ArticleWaitingForEditView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminPaneResponse {
    private List<ArticleInMakingView> articlesInMaking;
    private List<ArticleWaitingForEditView> articlesWaitingForEdit;
    private List<ArticleInEditView> articlesInEdit;
}
