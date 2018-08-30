package com.luangeng.oauthserver.dao;

import com.luangeng.oauthserver.vo.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {

}
