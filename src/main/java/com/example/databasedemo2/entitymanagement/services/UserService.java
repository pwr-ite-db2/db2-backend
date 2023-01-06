package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.entities.User;
import com.example.databasedemo2.entitymanagement.repositories.UserRepository;
import com.example.databasedemo2.entitymanagement.repositories.readonly.ArticleWaitingForEditViewRepository;
import com.example.databasedemo2.entitymanagement.repositories.readonly.ArticlesInEditViewRepository;
import com.example.databasedemo2.entitymanagement.repositories.readonly.ArticlesInMakingViewRepository;
import com.example.databasedemo2.entitymanagement.views.ArticleInEditView;
import com.example.databasedemo2.entitymanagement.views.ArticleInMakingView;
import com.example.databasedemo2.entitymanagement.views.ArticleWaitingForEditView;
import com.example.databasedemo2.frontendcommunication.custom.EditorPaneResponse;
import com.example.databasedemo2.security.UserAuthenticationInfoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService extends BaseService <User, Integer> implements UserDetailsService {
    private final ArticlesInEditViewRepository inEditViewRepository;
    private final ArticlesInMakingViewRepository inMakingViewRepository;
    private final ArticleWaitingForEditViewRepository waitingForEditViewRepository;
    private final UserAuthenticationInfoImpl userInfo;

    @Autowired
    public UserService(JpaRepository<User, Integer> repository, ArticlesInEditViewRepository inEditViewRepository,
                       ArticlesInMakingViewRepository inMakingViewRepository, UserAuthenticationInfoImpl userInfo,
                       ArticleWaitingForEditViewRepository waitingForEditViewRepository) {

        super(repository);
        this.inEditViewRepository = inEditViewRepository;
        this.inMakingViewRepository = inMakingViewRepository;
        this.userInfo = userInfo;
        this.waitingForEditViewRepository = waitingForEditViewRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return ((UserRepository) repository).findByEmail(username).orElseThrow();
    }

    public boolean existsByEmail(String email) {
        return ((UserRepository) repository).existsByEmail(email);
    }

    public List<ArticleInMakingView> getArticlesInMakingByCurrentUser() {
        User currentUser = userInfo.getAuthenticationInfo();
        return currentUser.getRole().getName().equals("AUTOR") ? inMakingViewRepository.findAllByUserId(currentUser.getId()) : null;
    }

    private List<ArticleInEditView> getArticlesInEditByCurrentUser() {
        User currentUser = userInfo.getAuthenticationInfo();
        return currentUser.getRole().getName().equals("REDAKTOR") ? inEditViewRepository.findAllByUserId(currentUser.getId()) : null;
    }

    private List<ArticleWaitingForEditView> getArticlesWaitingForEdit() {
        User currentUser = userInfo.getAuthenticationInfo();
        return currentUser.getRole().getName().equals("REDAKTOR") ? waitingForEditViewRepository.findAll() : null;
    }

    public EditorPaneResponse getEditorPane() {
        return new EditorPaneResponse(getArticlesInEditByCurrentUser(), getArticlesWaitingForEdit());
    }

    @Override
    public User addOrUpdate(User entity) {
        return null;
    }

    public User add(User entity) {
        return entity.getId() == 0 ? super.addOrUpdate(entity) : null;
    }

    public User update(User entity) {
        if (entity.getId() == 0)
            return null;

        if (entity.getPassword() == null) {
            String password = super.getById(entity.getId()).getPassword();
            entity.setPassword(password);
            entity.setUpdatedAt(new Date());
        }

        return super.addOrUpdate(entity);
    }
}