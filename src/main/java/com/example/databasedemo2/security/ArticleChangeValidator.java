package com.example.databasedemo2.security;

import com.example.databasedemo2.entitymanagement.entities.Article;
import com.example.databasedemo2.entitymanagement.entities.User;
import com.example.databasedemo2.entitymanagement.repositories.readonly.ArticlesInEditViewRepository;
import com.example.databasedemo2.exceptions.custom.AuthorizationException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ArticleChangeValidator {
    private final ArticlesInEditViewRepository inEditViewRepository;    // used to set editor of passed article

    public boolean isValidForEditing(Article article) {
        if (hasInvalidTitle(article))
            return false;

        // saved article
        if (article.getId() != 0)
            return article.getArticleStatus().getName().equals("UTWORZONY");

        // new article
        else
            return article.getArticleStatus() == null;  // new articles mustn't have status set
    }

    public boolean isValidForPublishing(Article article) {
        if (hasInvalidTitle(article))
            return false;

        // cannot publish articles
        if (article.getId() == 0)
            return false;

        // saved article
        else
            return (article.getArticleStatus() != null) &&
                    (article.getArticleStatus().getName().equals("REDAGOWANY") || article.getArticleStatus().getName().equals("WYCOFANY"));
    }

    public boolean isValidForRollback(Article article, User currentUser) {
        return article.getArticleStatus().getName().equals("REDAGOWANY") && isTheEditor(article, currentUser);
    }

    public boolean isValidForDeletion(Article article, User currentUser) {
        try {
            validateChanges(article, article, currentUser);
            return true;

        } catch (AuthorizationException e) {
            return false;
        }
    }

    // temporarily disabled function (articleNew unused)
    @SuppressWarnings("unused")
    public void validateChanges(Article articleOld, Article articleNew, User currentUser) throws AuthorizationException {
        String currentUserRole = currentUser.getRole().getName();

        // Author cannot edit articles that have already been submitted, and he cannot make changes to another authors article
        if ((!articleOld.getArticleStatus().getName().equals("UTWORZONY") && currentUserRole.equals("AUTOR")) ||
                (currentUserRole.equals("AUTOR") && !articleOld.getAuthor().equals(currentUser))) {

            throw new AuthorizationException();
        }

        // Same with editor
        if ((!articleOld.getArticleStatus().getName().equals("REDAGOWANY") && currentUserRole.equals("REDAKTOR")) ||
                (currentUserRole.equals("REDAKTOR") && !isTheEditor(articleOld, currentUser))) {

            throw new AuthorizationException();
        }

        // temporarily disable
        /*
         Flag any of these changes
         Author and editor mustn't change any of these fields:
         * article status
         * view count
         * remove date
         * author
         * comments
         * change history
                if (
                    articleOld.getViewCount() != articleNew.getViewCount() ||
                    ( (articleOld.getRemovedAt() != null && articleNew.getRemovedAt() != null) &&
                    (!articleOld.getRemovedAt().equals(articleNew.getRemovedAt())) ) ||
                    (!articleNew.isAllowStatusChange() && !articleOld.getArticleStatus().equals(articleNew.getArticleStatus())) ||
                    !articleOld.getAuthor().getEmail().equals(articleNew.getAuthor().getEmail()) ||
                    articleNew.getComments() != null ||
                    articleNew.getChangeHistory() != null
                ) {
                    throw new AuthorizationException();
                }
        */
    }

    private boolean hasInvalidTitle(Article article) {
        return article.getTitle() == null || article.getTitle().equals("");
    }

    private boolean isTheEditor(Article article, User editor) {
        return inEditViewRepository.findByArticleIdAndUserId(article.getId(),
                editor.getId()).isPresent();
    }
}