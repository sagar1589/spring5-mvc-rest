package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CategoryMapper;
import guru.springfamework.api.v1.model.CategoryDTO;
import guru.springfamework.domain.Category;
import guru.springfamework.exceptions.ResourceNotFoundException;
import guru.springfamework.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryMapper categoryMapper, CategoryRepository categoryRepository) {
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> categoryMapper.categoryToCategoryDTO(category))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryByName(String name) {
        return categoryMapper.categoryToCategoryDTO(categoryRepository.findByName(name));
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        Category savedCategory = categoryRepository.save(categoryMapper.categoryDTOToCategory(categoryDTO));
        CategoryDTO savedDTO = categoryMapper.categoryToCategoryDTO(savedCategory);
        return savedDTO;
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryMapper.categoryDTOToCategory(categoryDTO);
        category.setId(id);
        Category savedCategory = categoryRepository.save(category);
        CategoryDTO savedDTO = categoryMapper.categoryToCategoryDTO(savedCategory);
        return savedDTO;
    }

    @Override
    public CategoryDTO patchCategory(Long id, CategoryDTO categoryDTO) {
        return categoryRepository.findById(id)
                .map(category -> {
                    if (!category.getName().equals(categoryDTO.getName())){
                        category.setName(categoryDTO.getName());
                        categoryRepository.save(category);
                    }
                    return categoryMapper.categoryToCategoryDTO(category);
                }).orElseThrow(ResourceNotFoundException::new);
    }
}
