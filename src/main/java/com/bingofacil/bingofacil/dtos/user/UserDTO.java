package com.bingofacil.bingofacil.dtos.user;

import com.bingofacil.bingofacil.model.user.Address;

public record UserDTO (String username, String email, String password, String telephone, Address address)  {

}
