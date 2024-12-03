package com.team2.mosoo_backend.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGosu is a Querydsl query type for Gosu
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGosu extends EntityPathBase<Gosu> {

    private static final long serialVersionUID = -1673966912L;

    public static final QGosu gosu = new QGosu("gosu");

    public final StringPath businessName = createString("businessName");

    public final StringPath businessNumber = createString("businessNumber");

    public final StringPath category = createString("category");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath gender = createString("gender");

    public final StringPath gosuInfoAddress = createString("gosuInfoAddress");

    public final StringPath gosuInfoPhone = createString("gosuInfoPhone");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> points = createNumber("points", Integer.class);

    public final NumberPath<Long> technologyProviderId = createNumber("technologyProviderId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> userInfoId = createNumber("userInfoId", Long.class);

    public QGosu(String variable) {
        super(Gosu.class, forVariable(variable));
    }

    public QGosu(Path<? extends Gosu> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGosu(PathMetadata metadata) {
        super(Gosu.class, metadata);
    }

}

