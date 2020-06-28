package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Penelo extends _Default{
	public Penelo() {
		super("Penelo", null);
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{6785, 6786};
				break;
			case HP:
				ids = new int[]{6787, 6788};
				break;
			case S1:
				ids = new int[]{6923};
				break;
			case S2:
				ids = new int[]{6927};
				break;
			case EX:
				ids = new int[]{6973};
				break;
			case AA:
				ids = new int[]{6805};
				break;
			case LD:
				ids = new int[]{11035};
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