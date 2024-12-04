package com.team2.mosoo_backend.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGosu is a Querydsl query type for Gosu
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGosu extends EntityPathBase<Gosu> {

    private static final long serialVersionUID = -1673966912L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGosu gosu = new QGosu("gosu");

    public final StringPath businessName = createString("businessName");

    public final StringPath businessNumber = createString("businessNumber");

    public final com.team2.mosoo_backend.category.entity.QCategory category;

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath gender = createString("gender");

    public final StringPath gosuInfoAddress = createString("gosuInfoAddress");

    public final StringPath gosuInfoPhone = createString("gosuInfoPhone");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> points = createNumber("points", Integer.class);

    public final QUserInfo userInfo;

    public QGosu(String variable) {
        this(Gosu.class, forVariable(variable), INITS);
    }

    public QGosu(Path<? extends Gosu> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGosu(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGosu(PathMetadata metadata, PathInits inits) {
        this(Gosu.class, metadata, inits);
    }

    public QGosu(Class<? extends Gosu> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new com.team2.mosoo_backend.category.entity.QCategory(forProperty("category"), inits.get("category")) : null;
        this.userInfo = inits.isInitialized("userInfo") ? new QUserInfo(forProperty("userInfo"), inits.get("userInfo")) : null;
    }

}

