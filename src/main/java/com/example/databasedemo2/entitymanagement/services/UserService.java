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
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService <User, Integer> implements UserDetailsService {
    private final ArticlesInEditViewRepository inEditViewRepository;
    private final ArticlesInMakingViewRepository inMakingViewRepository;
    private final ArticleWaitingForEditViewRepository waitingForEditViewRepository;
    private final UserAuthenticationInfoImpl userInfo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, ArticlesInEditViewRepository inEditViewRepository,
                       ArticlesInMakingViewRepository inMakingViewRepository, UserAuthenticationInfoImpl userInfo,
                       ArticleWaitingForEditViewRepository waitingForEditViewRepository,
                       @Lazy PasswordEncoder passwordEncoder) {

        super(repository);
        this.inEditViewRepository = inEditViewRepository;
        this.inMakingViewRepository = inMakingViewRepository;
        this.userInfo = userInfo;
        this.waitingForEditViewRepository = waitingForEditViewRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, EntityNotFoundException {
        return ((UserRepository) repository).findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
    }

    public boolean existsByEmail(String email) {
        return ((UserRepository) repository).existsByEmail(email);
    }

    public List<ArticleInMakingView> getAuthorPane() {
        User currentUser = userInfo.getAuthenticationInfo();
        return inMakingViewRepository.findAllByUserId(currentUser.getId());
    }

    private List<ArticleInEditView> getArticlesInEditByCurrentUser() {
        User currentUser = userInfo.getAuthenticationInfo();
        return inEditViewRepository.findAllByUserId(currentUser.getId());
    }

    private List<ArticleWaitingForEditView> getArticlesWaitingForEdit() {
        return waitingForEditViewRepository.findAll();
    }

    public EditorPaneResponse getEditorPane() {
        return new EditorPaneResponse(getArticlesInEditByCurrentUser(), getArticlesWaitingForEdit());
    }

    @Override
    public User addOrUpdate(User entity, Map<String, String> params) {
        return null;
    }

    public User add(User entity) {
        return entity.getId() == 0 ? repository.save(entity) : null;
    }

    public User update(User entity, Map<String, String> params) {
        if (entity.getId() == 0)
            return null;

        User currentUser = userInfo.getAuthenticationInfo();
        boolean isAdmin = userInfo.isAdmin();

        // can't change these fields unless you are an admin
        if (!isAdmin) {
            entity.setRole(currentUser.getRole());
            entity.setEmail(currentUser.getEmail());
            entity.setCreatedAt(currentUser.getCreatedAt());
        }

        if (entity.getPassword() == null)
            entity.setPassword(currentUser.getPassword());
        else
            entity.setPassword(passwordEncoder.encode(entity.getPassword()));

        entity.setUpdatedAt(new Date());
        return super.addOrUpdate(entity, params);
    }
}