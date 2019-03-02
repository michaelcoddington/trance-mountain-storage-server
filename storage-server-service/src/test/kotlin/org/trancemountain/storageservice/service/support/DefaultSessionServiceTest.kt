package org.trancemountain.storageservice.service.support

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.reset
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.support.AnnotationConfigContextLoader
import org.trancemountain.storageservice.model.support.ClusterSessionInfo
import org.trancemountain.storageservice.model.support.DefaultSession
import org.trancemountain.storageservice.rest.Credentials
import org.trancemountain.storageservice.service.SessionService
import org.trancemountain.storageservice.service.clustering.ClusterSynchronizationService
import java.util.Optional

@ExtendWith(SpringExtension::class)
@ContextConfiguration(loader = AnnotationConfigContextLoader::class)
@DisplayName("a session service")
class DefaultSessionServiceTest {

    @Configuration
    @Import(DefaultSessionService::class, DefaultSession::class)
    internal class Config

    @Autowired
    private lateinit var sessionService: SessionService

    @MockBean
    private lateinit var mockAuthProvider: AuthenticationProvider

    @MockBean
    private lateinit var clusterService: ClusterSynchronizationService

    @BeforeEach
    private fun init() {
        reset(mockAuthProvider)
        `when`(mockAuthProvider.supports(UsernamePasswordAuthenticationToken::class.java)).thenReturn(true)
        reset(clusterService)
    }

    @Test
    @DisplayName("should return a session for an authorized user")
    fun getSessionForAuthUser() {
        val auth = mock(Authentication::class.java)
        `when`(auth.isAuthenticated).thenReturn(true)
        `when`(auth.name).thenReturn("user")
        `when`(mockAuthProvider.authenticate(any())).thenReturn(auth)
        val creds = mock(Credentials::class.java)
        `when`(creds.username).thenReturn("user")
        `when`(creds.password).thenReturn("pass")
        val session = sessionService.getSession(creds)
        assertTrue(session.isPresent, "session not returned")
    }

    @Test
    @DisplayName("should not return a session for an authorized user without a username")
    fun getSessionWithNoUsername() {
        val creds = mock(Credentials::class.java)
        `when`(creds.password).thenReturn("pass")
        val session = sessionService.getSession(creds)
        assertFalse(session.isPresent, "session returned")
    }

    @Test
    @DisplayName("should not return a session for an authorized user without a password")
    fun getSessionWithNoPassword() {
        val creds = mock(Credentials::class.java)
        `when`(creds.username).thenReturn("user")
        val session = sessionService.getSession(creds)
        assertFalse(session.isPresent, "session returned")
    }

    @Test
    @DisplayName("should not return a session for an unauthorized user")
    fun getSessionForUnauthUser() {
        val auth = mock(Authentication::class.java)
        `when`(auth.isAuthenticated).thenReturn(false)
        `when`(mockAuthProvider.authenticate(any())).thenReturn(auth)
        val creds = mock(Credentials::class.java)
        `when`(creds.username).thenReturn("user")
        `when`(creds.password).thenReturn("pass")
        val session = sessionService.getSession(creds)
        assertFalse(session.isPresent, "session returned")
    }

    @Test
    @DisplayName("should be able to get a session with a valid token")
    fun getSessionForToken() {
        val info = ClusterSessionInfo("token", "userID")
        `when`(clusterService.getSessionInfo(eq("token"))).thenReturn(Optional.of(info))
        val session = sessionService.getSession("token")
        assertTrue(session.isPresent, "session not returned")
    }

    @Test
    @DisplayName("should not be able to get a session with an invalid token")
    fun getSessionForInvalidToken() {
        `when`(clusterService.getSessionInfo(eq("token"))).thenReturn(Optional.empty())
        val session = sessionService.getSession("token")
        assertFalse(session.isPresent, "session not returned")
    }

}