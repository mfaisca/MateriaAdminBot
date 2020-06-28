package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Kimahri extends _Default{
	public Kimahri() {
		super("Kimahri", "furry", "furry");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{9714};
				break;
			case HP:
				ids = new int[]{5891};
				break;
			case S1:
				ids = new int[]{5880, 5881};
				break;
			case S2:
				ids = new int[]{5888, 5889};
				break;
			case EX:
				ids = new int[]{5898};
				break;
			case AA:
				ids = new int[]{5902};
				break;
			case LD:
				ids = new int[]{9577};
				break;
			case BT:
				ids = new int[]{};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}