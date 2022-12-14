
package com.gft.manager.controller;

import com.gft.manager.model.CustomUserDetails;
import com.gft.manager.model.payload.ApiResponse;
import com.gft.manager.model.payload.UpdatePasswordRequest;
import com.gft.manager.annotation.CurrentUser;
import com.gft.manager.event.OnUserAccountChangeEvent;
import com.gft.manager.event.OnUserLogoutSuccessEvent;
import com.gft.manager.exception.UpdatePasswordException;
import com.gft.manager.service.impl.AuthService;
import com.gft.manager.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/user")
@Tag(name = "User Rest API", description = "Defines endpoints for the logged in user. It's secured by default")

public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class);

    private final AuthService authService;

    private final UserService userService;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public UserController(AuthService authService, UserService userService, ApplicationEventPublisher applicationEventPublisher) {
        this.authService = authService;
        this.userService = userService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Gets the current user profile of the logged in user
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @Operation(description ="Returns the current user profile")
    public ResponseEntity getUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails principal = (CustomUserDetails) auth.getPrincipal();
        logger.info(principal.getUsername() + " has role: " + principal.getRoles());

//        return ResponseEntity.ok().body(principal.getFullName());
        return ResponseEntity.ok("Hello. This is about me "+principal.getFullName());
    }

//    public ResponseEntity getUser(){
//
//    }


    /**
     * Returns all admins in the system. Requires Admin access
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(description = "Returns the list of configured admins. Requires ADMIN Access")
    public ResponseEntity getAllAdmins() {
        logger.info("Inside secured resource with admin");
        return ResponseEntity.ok(userService.findAll());
    }

    /**
     * Updates the password of the current logged in user
     */
    @PostMapping("/password/update")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(description = "Allows the user to change his password once logged in by supplying the correct current " +
            "password")
    public ResponseEntity updateUserPassword(@CurrentUser CustomUserDetails customUserDetails,
                                             @Parameter(description = "The UpdatePasswordRequest payload") @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {

        return authService.updatePassword(customUserDetails, updatePasswordRequest)
                .map(updatedUser -> {
                    OnUserAccountChangeEvent onUserPasswordChangeEvent = new OnUserAccountChangeEvent(updatedUser, "Update Password", "Change successful");
                    applicationEventPublisher.publishEvent(onUserPasswordChangeEvent);
                    return ResponseEntity.ok(new ApiResponse(true, "Password changed successfully"));
                })
                .orElseThrow(() -> new UpdatePasswordException("--Empty--", "No such user present."));
    }

    /**
     * Log the user out from the app/device. Release the refresh token associated with the
     * user device.
     */
    @PostMapping("/logout")
    @Operation(description = "Logs the specified user device and clears the refresh tokens associated with it")
    public ResponseEntity logoutUser(@CurrentUser CustomUserDetails customUserDetails) {

//            DeviceInfo deviceInfo = new DeviceInfo();
//            deviceInfo.setDeviceId("258");
//            deviceInfo.setDeviceType(DeviceType.DEVICE_TYPE_WEB);
//            LogOutRequest logOutRequest = new LogOutRequest();
//            logOutRequest.setDeviceInfo(deviceInfo);

//        userService.logoutUser(customUserDetails, logOutRequest);
        Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();

        OnUserLogoutSuccessEvent logoutSuccessEvent = new OnUserLogoutSuccessEvent(customUserDetails.getEmail(), credentials.toString());
        applicationEventPublisher.publishEvent(logoutSuccessEvent);
        return ResponseEntity.ok(new ApiResponse(true, "Log out successful"));
    }
    @PostMapping("/disable/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity disableUser(@PathVariable(required = true) String email){
           if (userService.userAccountDisable(email)){
               return ResponseEntity.ok(new ApiResponse(true,"user["+email+"] account disable "));
        }else
            return ResponseEntity.badRequest().body(new ApiResponse(false,email +" user not found"));
    }
    @PostMapping("/enable/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity enableUser(@PathVariable(required = true) String email){
        if (userService.userAccountEnable(email)){
            return ResponseEntity.ok(new ApiResponse(true,"user["+email+"] account enable "));
        }else
            return ResponseEntity.badRequest().body(new ApiResponse(false,email +" user not found"));
    }
    @GetMapping("/reviewers/{searchKey}")
    @Operation(description = "search by username or email or full name")
    public ResponseEntity findByReviewerName(@PathVariable String searchKey){
        var respon = userService.findAllReviewersByUserName(searchKey);
        return ResponseEntity.status((int) respon.getStatusCode()).body(respon);
    }

    @PostMapping("/reviewers/reviewer/{searchKey}")
    @Operation(description = "search by username or email or full name get response with detail")
    public ResponseEntity findByReviewer(@PathVariable String searchKey){
        if (searchKey.isEmpty() || searchKey == null) return ResponseEntity.badRequest().body("searchKey can not empty");
        var respon = userService.findAllReviewersWithDetailsBySearchKey(searchKey);
        return ResponseEntity.status((int) respon.getStatusCode()).body(respon);
    }
    @GetMapping("/influencers/{searchKey}")
    @Operation(description = "search by username or email or full name")
    public ResponseEntity findByIInfluencersName(@PathVariable String searchKey){
        var respon = userService.finAllInfluencersByUserName(searchKey);
        return ResponseEntity.status((int) respon.getStatusCode()).body(respon);
    }

}
