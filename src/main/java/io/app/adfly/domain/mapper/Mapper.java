package io.app.adfly.domain.mapper;

import io.app.adfly.domain.dto.*;
import io.app.adfly.entities.Product;
import io.app.adfly.entities.ProductAdvertiser;
import io.app.adfly.entities.User;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class Mapper {
    private static org.modelmapper.ModelMapper modelMapper;

    static {
        modelMapper = new org.modelmapper.ModelMapper();

        modelMapper.typeMap(Product.class, ProductDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getProductRewarding(),
                    ProductDto::setProductRewarding);
            mapper.map(src -> src.getSite(),
                    ProductDto::setSite);
        });
        modelMapper.typeMap(PaginatedRequest.class, Pageable.class).setConverter(PaginatedRequestToPageableConverter());
        modelMapper.typeMap(ProductAdvertiser.class, ProductAdvertiserDto.class).setConverter(ProductAdvertiserToProductAdvertiserDto());

    }

    public static  <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }

    public static <T> T map(Object source, Class<T> destinationType){
        return modelMapper.map(source, destinationType);
    }

    public static void map(Object source, Object destination){
        modelMapper.map(source, destination);
    }

    public static <T> PaginatedResponse<T> mapPaginatedResponse(List<T> list, PaginatedRequest request, int totalCount){
        var paginateResponse = new PaginatedResponse<T>();
        paginateResponse.setData(list);
        paginateResponse.setStartAt(request.getStartAt());
        paginateResponse.setCount(request.getCount());
        paginateResponse.setTotalCount(totalCount);
        return paginateResponse;
    }

    private static AbstractConverter<PaginatedRequest, Pageable> PaginatedRequestToPageableConverter(){
        return new AbstractConverter<>() {
            @Override
            protected Pageable convert(PaginatedRequest request) {
                int pageNumber = request.getStartAt()/request.getCount();
                Pageable paging = PageRequest.of(pageNumber, request.getCount(), Sort.by("id"));
                return paging;
            }
        };

    }

    private static AbstractConverter<ProductAdvertiser, ProductAdvertiserDto> ProductAdvertiserToProductAdvertiserDto(){
        return new AbstractConverter<>() {
            @Override
            protected ProductAdvertiserDto convert(ProductAdvertiser productAdvertiser) {
                var productAdvertiserDto = new ProductAdvertiserDto();
                productAdvertiserDto.setProduct(modelMapper.map(productAdvertiser.getProduct(), ProductDto.class));
                productAdvertiserDto.setId(productAdvertiser.getId());
                productAdvertiserDto.setUrl("http://localhost:8080/s/"+ productAdvertiser.getRedirectCode());
                return productAdvertiserDto;
            }
        };

    }
}
