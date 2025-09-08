package ssu.cromi.teamit.repository.postboard;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ssu.cromi.teamit.entity.enums.PostCategory;
import ssu.cromi.teamit.entity.postboard.Post;
import ssu.cromi.teamit.entity.postboard.QPost;
import java.util.List;

@RequiredArgsConstructor
public class PostSearchRepositoryImpl implements PostSearchRepository{

    private final JPAQueryFactory queryFactory;
    private final QPost post = QPost.post;

    @Override
    public Page<Post> searchPosts(PostCategory category, String keyword, Pageable pageable) {
        List<Post> content = queryFactory
                .selectFrom(post)
                .where(
                        categoryEq(category),
                        keywordContains(keyword)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createdAt.desc()) // 최신순
                .fetch();

        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(
                        categoryEq(category),
                        keywordContains(keyword)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }

    private BooleanExpression categoryEq(PostCategory category) {
        return category != null ? post.category.eq(category) : null;
    }

    // 제목 검색
    private BooleanExpression keywordContains(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return null;
        }
        return Expressions.stringTemplate("function('REPLACE', {0}, ' ', '')", post.title)
                .containsIgnoreCase(keyword.replaceAll("\\s+", ""));
    }
}