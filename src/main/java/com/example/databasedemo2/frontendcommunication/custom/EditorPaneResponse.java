package com.example.databasedemo2.frontendcommunication.custom;

import com.example.databasedemo2.entitymanagement.views.ArticleInEditView;
import com.example.databasedemo2.entitymanagement.views.ArticleWaitingForEditView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditorPaneResponse {
    private List<ArticleInEditView> articlesInEditByCurrentUser;
    private List<ArticleWaitingForEditView> articlesWaitingForEdit;
}
