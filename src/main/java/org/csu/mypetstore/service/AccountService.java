package org.csu.mypetstore.service;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.persistence.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;

public interface AccountService {

    Account getAccount(String username) ;

    Account getAccount(String username, String password) ;

    public void insertAccount(Account account) ;

    public void updateAccount(Account account) ;
}
