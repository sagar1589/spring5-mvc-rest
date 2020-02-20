package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.VendorMapper;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.controllers.v1.VendorController;
import guru.springfamework.domain.Vendor;
import guru.springfamework.exceptions.ResourceNotFoundException;
import guru.springfamework.repositories.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    private final VendorMapper vendorMapper;

    public VendorServiceImpl(VendorRepository vendorRepository, VendorMapper vendorMapper) {
        this.vendorRepository = vendorRepository;
        this.vendorMapper = vendorMapper;
    }


    @Override
    public List<VendorDTO> getAllVendors() {
        return vendorRepository.findAll().stream()
                .map(vendor -> {
                    VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor);
                    vendorDTO.setVendorUrl(VendorController.BASE_URL + "/"+vendor.getId());
                    return vendorDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public VendorDTO getVendorById(Long id) {
        return vendorRepository.findById(id)
                .map(vendor -> {
                    VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor);
                    vendorDTO.setVendorUrl(VendorController.BASE_URL + "/"+vendor.getId());
                    return vendorDTO;
                }).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public VendorDTO addVendor(VendorDTO vendorDTO) {
        Vendor vendor =  vendorRepository.save(vendorMapper.vendorDtoToVendor(vendorDTO));
        VendorDTO savedVendorDTO = vendorMapper.vendorToVendorDTO(vendor);
        savedVendorDTO.setVendorUrl(VendorController.BASE_URL + "/"+vendor.getId());
        return savedVendorDTO;
    }

    @Override
    public VendorDTO updateVendor(Long id, VendorDTO vendorDTO) {
       Vendor vendor = vendorMapper.vendorDtoToVendor(vendorDTO);
       vendor.setId(id);
       Vendor savedVendor = vendorRepository.save(vendor);
       VendorDTO savedVendorDTO = vendorMapper.vendorToVendorDTO(vendor);
       savedVendorDTO.setVendorUrl(VendorController.BASE_URL + "/"+savedVendor.getId());
       return savedVendorDTO;
    }

    @Override
    public VendorDTO patchVendor(Long id, VendorDTO vendorDTO) {
        return vendorRepository.findById(id)
                .map(vendor -> {
                    if (vendorDTO.getName()!= null){
                        vendor.setName(vendorDTO.getName());
                    }
                    Vendor savedVendor = vendorRepository.save(vendor);
                    VendorDTO savedVendorDTO = vendorMapper.vendorToVendorDTO(vendor);
                    savedVendorDTO.setVendorUrl(VendorController.BASE_URL + "/"+savedVendor.getId());
                    return savedVendorDTO;
                }).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void deleteVendor(Long id) {
        vendorRepository.deleteById(id);
    }
}
