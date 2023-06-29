package com.mgsystems.crudclient.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgsystems.crudclient.dto.ClientDTO;
import com.mgsystems.crudclient.entities.Client;
import com.mgsystems.crudclient.repositories.ClientRepository;
import com.mgsystems.crudclient.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;


@Service
public class ClientService {
	
	@Autowired
	private ClientRepository repository;

	@Transactional(readOnly = true)
	public Page<ClientDTO> findAll(Pageable pageable) {
		
		Page<Client> pageCliet = repository.findAll(pageable);
		
		Page<ClientDTO> pageDTO = pageCliet.map(x -> new ClientDTO(x));
		
		return pageDTO;
	}

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		
		Client entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
		
		return new ClientDTO(entity);
	}

	@Transactional
	public void deleteById(Long id) {
		
		try {
			repository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
		
	}

	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		
		Client entity= new Client();
		copyDTOToEntity(dto, entity);
		entity = repository.save(entity);
		
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		
		try {
			Client entity = repository.getReferenceById(id);
			copyDTOToEntity(dto, entity);
			entity = repository.save(entity); 
			
			return new ClientDTO(entity);
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
	}

	private void copyDTOToEntity(ClientDTO dto, Client entity) {
		
		entity.setBirthDate(dto.getBirthDate());
		entity.setChildren(dto.getChildren());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setName(dto.getName());
		
	}

}
