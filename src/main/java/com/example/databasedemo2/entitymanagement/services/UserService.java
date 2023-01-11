package com.example.databasedemo2.entitymanagement.services;

import com.example.databasedemo2.entitymanagement.entities.Comment;
import com.example.databasedemo2.entitymanagement.entities.User;
import com.example.databasedemo2.entitymanagement.repositories.CommentRepository;
import com.example.databasedemo2.entitymanagement.repositories.UserRepository;
import com.example.databasedemo2.entitymanagement.repositories.readonly.ArticleWaitingForEditViewRepository;
import com.example.databasedemo2.entitymanagement.repositories.readonly.ArticlesInEditViewRepository;
import com.example.databasedemo2.entitymanagement.repositories.readonly.ArticlesInMakingViewRepository;
import com.example.databasedemo2.entitymanagement.views.ArticleInEditView;
import com.example.databasedemo2.entitymanagement.views.ArticleInMakingView;
import com.example.databasedemo2.entitymanagement.views.ArticleWaitingForEditView;
import com.example.databasedemo2.exceptions.custom.AuthorizationException;
import com.example.databasedemo2.frontendcommunication.customjson.AdminPaneResponse;
import com.example.databasedemo2.frontendcommunication.customjson.dtolayer.CommentDTO;
import com.example.databasedemo2.frontendcommunication.customjson.EditorPaneResponse;
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
import java.util.stream.Collectors;

@Service
public class UserService extends BaseService <User, Integer> implements UserDetailsService {
    private final ArticlesInEditViewRepository inEditViewRepository;
    private final ArticlesInMakingViewRepository inMakingViewRepository;
    private final ArticleWaitingForEditViewRepository waitingForEditViewRepository;
    private final UserAuthenticationInfoImpl userInfo;
    private final PasswordEncoder passwordEncoder;
    private final CommentRepository commentRepository;

    @Autowired
    public UserService(UserRepository repository, ArticlesInEditViewRepository inEditViewRepository,
                       ArticlesInMakingViewRepository inMakingViewRepository, UserAuthenticationInfoImpl userInfo,
                       ArticleWaitingForEditViewRepository waitingForEditViewRepository,
                       @Lazy PasswordEncoder passwordEncoder, CommentRepository commentRepository) {

        super(repository);
        this.inEditViewRepository = inEditViewRepository;
        this.inMakingViewRepository = inMakingViewRepository;
        this.userInfo = userInfo;
        this.waitingForEditViewRepository = waitingForEditViewRepository;
        this.passwordEncoder = passwordEncoder;
        this.commentRepository = commentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, EntityNotFoundException {
        return ((UserRepository) repository).findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
    }

    public boolean existsByEmail(String email) {
        return ((UserRepository) repository).existsByEmail(email);
    }

    public List<?> getWorkingPane() throws AuthorizationException {
        User currentUser = userInfo.getAuthenticationInfo();
        return switch (currentUser.getRole().getName()) {
            case "AUTOR" -> getArticlesInMakingByUser(currentUser.getId());
            case "REDAKTOR" -> List.of(getEditorPane(currentUser.getId()));
            case "ADMIN" -> List.of(getAdminPane());
            default -> throw new AuthorizationException();
        };
    }

    private List<ArticleInMakingView> getArticlesInMakingByUser(int userId) {
        return inMakingViewRepository.findAllByUserId(userId);
    }

    private List<ArticleInEditView> getArticlesInEditByUser(int userId) {
        return inEditViewRepository.findAllByUserId(userId);
    }

    private List<ArticleWaitingForEditView> getArticlesWaitingForEdit() {
        return waitingForEditViewRepository.findAll();
    }

    private List<CommentDTO> getFlaggedComments() {
        List<Comment> flaggedComments = commentRepository.findAllByReported(true);
        return flaggedComments
                .stream()
                .map(CommentDTO::buildFromComment)
                .collect(Collectors.toList());
    }

    private EditorPaneResponse getEditorPane(int userId) {
        return new EditorPaneResponse(getArticlesInEditByUser(userId), getArticlesWaitingForEdit());
    }

    private AdminPaneResponse getAdminPane() {
        return new AdminPaneResponse(inMakingViewRepository.findAll(), waitingForEditViewRepository.findAll(), inEditViewRepository.findAll(), getFlaggedComments());
    }

    public User getCurrentUserInfo() {
        return userInfo.getAuthenticationInfo();
    }

    @Override
    public User addOrUpdate(User entity, Map<String, String> params) throws EntityNotFoundException {
        return null;
    }

    public User add(User entity) {
        // to update User use update method
        return entity.getId() == 0 ? repository.save(entity) : null;
    }

    public User update(User entity, Map<String, String> params, boolean isAdmin) throws AuthorizationException {
        // to create new User use add method
        if (entity.getId() == 0)
            return null;

        User currentUser = userInfo.getAuthenticationInfo();

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

    public User updateCurrentUserInfo(User user, Map<String, String> params) throws AuthorizationException {
        User currentUser = userInfo.getAuthenticationInfo();

        // validate user
        if (currentUser.getId() != user.getId())
            throw new AuthorizationException();

        return update(user, params, false);
    }
}