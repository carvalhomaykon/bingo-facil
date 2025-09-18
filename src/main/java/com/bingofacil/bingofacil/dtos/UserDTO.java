package com.bingofacil.bingofacil.dtos;

import com.bingofacil.bingofacil.model.user.Address;

public record UserDTO (String name, String email, String password, String telephone, String firstName, String lastName, Address address)  {

}
